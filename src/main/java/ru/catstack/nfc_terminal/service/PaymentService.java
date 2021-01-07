package ru.catstack.nfc_terminal.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.exception.BadRequestException;
import ru.catstack.nfc_terminal.exception.ResourceNotFoundException;
import ru.catstack.nfc_terminal.model.Company;
import ru.catstack.nfc_terminal.model.Payment;
import ru.catstack.nfc_terminal.model.enums.PaymentStatus;
import ru.catstack.nfc_terminal.model.payload.request.CreatePaymentRequest;
import ru.catstack.nfc_terminal.model.payload.request.ReturnPaymentRequest;
import ru.catstack.nfc_terminal.repository.PaymentRepository;

import java.io.IOException;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CompanyService companyService;
    private final ReceiptService receiptService;
    private final EmailService emailService;
    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, CompanyService companyService, ReceiptService receiptService, EmailService emailService, SessionService sessionService, UserService userService) {
        this.paymentRepository = paymentRepository;
        this.companyService = companyService;
        this.receiptService = receiptService;
        this.emailService = emailService;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    public PaymentStatus acceptPayment(@NotNull CreatePaymentRequest rq) throws IOException {
        if (!companyService.existsByInn(rq.getInn()))
            throw new ResourceNotFoundException("Company", "INN", rq.getInn());

        if (paymentRepository.existsByIdempotenceKey(rq.getIdempotenceKey()))
            return paymentRepository.findByIdempotenceKey(rq.getIdempotenceKey()).get().getStatus();

        var me = userService.getLoggedInUser();

        if (sessionService.findByUserIdAndDeviceId(me.getId(), rq.getDeviceInfo().getDeviceId()).isEmpty())
            throw new BadRequestException("Payment impossible");

        var newPayment = new Payment(rq.getIdempotenceKey(), rq.getTitle(), rq.getPayerCN(), rq.getInn(), rq.getCost(), rq.getAmount(), rq.getDeviceInfo().getDeviceId(), rq.getBuyerEmail());
        paymentRepository.save(newPayment);
        var payment = paymentRepository.findByIdempotenceKey(newPayment.getIdempotenceKey()).get();
        var confirmation = GetBankConfirmation(rq.getPayerCN(), rq.getCost());
        if (confirmation) {
            var company = companyService.findByInn(rq.getInn());
            if (company.isEmpty())
                throw new ResourceNotFoundException("Comapny", "INN", rq.getInn());
            companyService.addToBalance(company.get(), rq.getCost());
            paymentRepository.updateStatusByIdempotenceKey(rq.getIdempotenceKey(), PaymentStatus.SUCCESSFULLY);
            var receipt = receiptService.createReceipt(payment);
            if (payment.getBuyerEmail() != null)
                try {
                    emailService.sendReceiptMail(receipt);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new BadRequestException("К сожалению, не удалось отправить чек. Попробуйте позже");
                }
            return PaymentStatus.SUCCESSFULLY;
        }

        return PaymentStatus.DENIED;
    }

    public PaymentStatus returnPayment(ReturnPaymentRequest rq) {
        var me = userService.getLoggedInUser();
        if (sessionService.findByUserIdAndDeviceId(me.getId(), rq.getDeviceInfo().getDeviceId()).isEmpty())
            throw new BadRequestException("Payment impossible");

        var payment = paymentRepository.findById(rq.getPaymentId());

        if (payment.isEmpty())
            throw new ResourceNotFoundException("Payment", "ID", rq.getPaymentId());

        if (payment.get().getStatus() == PaymentStatus.RETURNED)
            throw new BadRequestException("Payment was already returned to payer");

        if (payment.get().getVendorId() != rq.getInn()
                || !payment.get().getDeviceId().equals(rq.getDeviceInfo().getDeviceId())
                || payment.get().getPayerCardNumber() != rq.getPayerCN())
            throw new BadRequestException("Unable to return payment");

        paymentRepository.updateStatusByIdempotenceKey(payment.get().getIdempotenceKey(), PaymentStatus.RETURNED);
        return PaymentStatus.RETURNED;
    }

    public Page<Payment> findAllByCompany(Company company, Pageable pageable) {
        return paymentRepository.findAllByVendorId(company.getInn(), pageable);
    }

    private boolean GetBankConfirmation(long payerCN, float amount) {
        return true;
    }

}
