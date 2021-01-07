package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.model.Employee;
import ru.catstack.nfc_terminal.model.HistoryRecord;
import ru.catstack.nfc_terminal.model.Payment;
import ru.catstack.nfc_terminal.util.OffsetBasedPage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {
    private final PaymentService paymentService;
    private final Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
    private final UserService userService;

    @Autowired
    public HistoryService(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    public List<HistoryRecord> getRecordsGap(int from, int count) {
        var me = userService.getLoggedInUser();
        var employee = (Employee) me.getRegistrations().toArray()[0];
        var receipts = paymentService.findAllByCompany(employee.getCompany(), new OffsetBasedPage(from, count, sort)).getContent();
        return receipts.stream().map(this::receiptToHistoryRecord).collect(Collectors.toList());
    }

    private HistoryRecord receiptToHistoryRecord(Payment payment) {
        var iconCode = payment.getTitle().toLowerCase().split(" ")[0];
        return new HistoryRecord(payment.getId(), payment.getStatus(), payment.getTitle(), payment.getCost(), iconCode, payment.getCreatedAt());
    }
}
