package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.exception.ForbiddenException;
import ru.catstack.nfc_terminal.exception.ResourceNotFoundException;
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
        if (!companyService.existsByInn(payment.getVendorId()))
            throw new ResourceNotFoundException("Company", "INN", payment.getVendorId());
        var company = companyService.findByInn(payment.getVendorId()).get();
        return sessionService.findByUserIdAndDeviceId(me.getId(), payment.getDeviceId()).map(
                s -> {
                    var totalCost = payment.getCost() * payment.getAmount();
                    var buyerEmail = payment.getBuyerEmail() == null ? "Не указано" : payment.getBuyerEmail();
                    var r = new Receipt(s, company, me, payment.getTitle(), payment.getCost(), payment.getAmount(), totalCost, buyerEmail);
                    return receiptRepository.save(r);
                }
        ).orElseThrow(() -> new ForbiddenException("Device id is invalid"));

    }}
