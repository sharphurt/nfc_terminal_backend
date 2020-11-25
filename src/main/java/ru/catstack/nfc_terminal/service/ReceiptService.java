package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.model.Payment;
import ru.catstack.nfc_terminal.model.Receipt;
import ru.catstack.nfc_terminal.repository.ReceiptRepository;

@Service
public class ReceiptService {
    private final UserService userService;
    private final SessionService sessionService;
    private final ReceiptRepository receiptRepository;
    private final CompanyService companyService;

    @Autowired
    public ReceiptService(UserService userService, SessionService sessionService, ReceiptRepository receiptRepository, CompanyService companyService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.receiptRepository = receiptRepository;
        this.companyService = companyService;
    }

    public Receipt createReceipt(Payment payment) {
        var me = userService.getLoggedInUser();
        var company = companyService.findByInn(payment.getVendorId()).get();
        var session = sessionService.findByUserIdAndDeviceId(me.getId(), payment.getDeviceId()).get();
        var totalCost = payment.getAmount() * 1;
        var buyerEmail = payment.getBuyerEmail() == null ? "Не указано" : payment.getBuyerEmail();
        var r = new Receipt(session, company, me, "Покупка товаров", payment.getAmount(), 1, totalCost, buyerEmail, 123434, 2324);
        return receiptRepository.save(r);
    }}
