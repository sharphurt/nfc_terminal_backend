package ru.catstack.nfc_terminal.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.exception.ResourceNotFoundException;
import ru.catstack.nfc_terminal.model.Payment;
import ru.catstack.nfc_terminal.model.enums.PaymentStatus;
import ru.catstack.nfc_terminal.model.payload.request.CreatePaymentRequest;
import ru.catstack.nfc_terminal.repository.PaymentRepository;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CompanyService companyService;
    private final ReceiptService receiptService;
    private final EmailService emailService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, CompanyService companyService, ReceiptService receiptService, EmailService emailService) {
        this.paymentRepository = paymentRepository;
        this.companyService = companyService;
        this.receiptService = receiptService;
        this.emailService = emailService;
    }

    public PaymentStatus acceptPayment(@NotNull CreatePaymentRequest rq) {
        if (!companyService.existsByInn(rq.getInn()))
            throw new ResourceNotFoundException("Company", "INN", rq.getInn());

        if (paymentRepository.existsByTransactionalKey(rq.getTransactionalKey()))
            return paymentRepository.findByTransactionalKey(rq.getTransactionalKey()).get().getStatus();

        var payment = new Payment(rq.getTransactionalKey(), rq.getPayerCN(), rq.getInn(), rq.getAmount(), rq.getDeviceInfo().getDeviceId(), rq.getBuyerEmail());
        paymentRepository.save(payment);

        var confirmation = GetBankConfirmation(rq.getPayerCN(), rq.getAmount());
        if (confirmation) {
            var company = companyService.findByInn(rq.getInn()).get();
            companyService.addToBalance(company, rq.getAmount());
            paymentRepository.updateStatusByTransactionalKey(rq.getTransactionalKey(), PaymentStatus.SUCCESSFULLY);
            var receipt = receiptService.createReceipt(payment);
            if (payment.getBuyerEmail() != null)
                emailService.sendMail(receipt);
            return PaymentStatus.SUCCESSFULLY;
        }

        return PaymentStatus.DENIED;
    }

    private boolean GetBankConfirmation(long payerCN, float amount) {
        return true;
    }

}
