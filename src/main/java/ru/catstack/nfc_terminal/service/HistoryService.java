package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.model.Employee;
import ru.catstack.nfc_terminal.model.HistoryRecord;
import ru.catstack.nfc_terminal.model.Receipt;
import ru.catstack.nfc_terminal.util.OffsetBasedPage;
import ru.catstack.nfc_terminal.util.Util;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {
    private final ReceiptService receiptService;
    private final Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
    private final UserService userService;

    @Autowired
    public HistoryService(ReceiptService receiptService, UserService userService) {
        this.receiptService = receiptService;
        this.userService = userService;
    }

    public List<HistoryRecord> getRecordsGap(int from, int count) {
        var me = userService.getLoggedInUser();
        var employee = (Employee) me.getRegistrations().toArray()[0];
        var receipts = receiptService.findAllByCompany(employee.getCompany(), new OffsetBasedPage(from, count, sort)).getContent();
        return receipts.stream().map(this::receiptToHistoryRecord).collect(Collectors.toList());
    }

    private HistoryRecord receiptToHistoryRecord(Receipt receipt) {
        var iconPath = "src/main/resources/card_inc_logos/"
                + receipt.getProductName().toLowerCase().split(" ")[0]
                + ".png";

        String iconCode = Util.readFile("src/main/resources/card_inc_logos/empty.txt");
        if (new File(iconPath).exists()) {
            var code = Util.imageToBase64(iconPath);
            if (code != null)
                iconCode = code;
        }

        return new HistoryRecord(receipt.getProductName(), receipt.getProductCost(), iconCode, receipt.getCreatedAt());
    }
}
