package ru.catstack.nfc_terminal.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.exception.BadRequestException;
import ru.catstack.nfc_terminal.exception.ResourceNotFoundException;
import ru.catstack.nfc_terminal.model.Payment;
import ru.catstack.nfc_terminal.model.enums.PaymentStatus;
import ru.catstack.nfc_terminal.model.payload.request.CreatePaymentRequest;
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

        var payment = new Payment(rq.getIdempotenceKey(), rq.getTitle(), rq.getPayerCN(), rq.getInn(), rq.getCost(), rq.getAmount(), rq.getDeviceInfo().getDeviceId(), rq.getBuyerEmail());
        paymentRepository.save(payment);

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
                    throw new BadRequestException("Email service exception");
                }
            return PaymentStatus.SUCCESSFULLY;
        }

        return PaymentStatus.DENIED;
    }

    private boolean GetBankConfirmation(long payerCN, float amount) {
        return true;
    }

}
