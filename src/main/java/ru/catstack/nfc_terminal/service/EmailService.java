package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.exception.ResourceNotFoundException;
import ru.catstack.nfc_terminal.model.Receipt;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(Receipt receipt) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String htmlCode;
        try {
            var scanner = new Scanner(new File("src/main/resources/email_temaplate/template.html"));
            htmlCode = scanner.useDelimiter("\\A").next();
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new ResourceNotFoundException("File", "path", "resources/template.html");
        }

        var data = getDataFromReceipt(receipt);
        var html = insertDataToHTML(data, htmlCode);

        try {
            helper.setText(html, true); // Use this or above line.
            helper.setTo(data.get("recipient-email"));
            helper.setSubject("Кассовый чек о покупке");
            helper.setFrom(data.get("sender-email"));
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, String> getDataFromReceipt(Receipt receipt) {
        var data = new HashMap<String, String>();
        data.put("company-name", receipt.getCompany().getCompanyName());
        data.put("company-address", receipt.getCompany().getAddress());
        data.put("inn-number", String.valueOf(receipt.getCompany().getInn()));
        data.put("tax-system", receipt.getCompany().getTaxSystem());
        data.put("receipt-per-shift-number", String.valueOf(receipt.getSession().getReceipts().size()));
        data.put("shift-number", String.valueOf(receipt.getVendor().getLoginsCount()));

        var vendorFullname = receipt.getVendor().getLastName() + " " + receipt.getVendor().getFirstName();
        if (receipt.getVendor().getPatronymic() != null)
            vendorFullname += " " + receipt.getVendor().getPatronymic();

        var formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        var dateTime = formatter.format(new Date(System.currentTimeMillis()));
        data.put("date-time", dateTime);
        data.put("vendor-fullname", vendorFullname);
        data.put("product-name", receipt.getProductName());
        data.put("product-cost", String.format("%.2f", receipt.getProductCost()));
        data.put("product-amount", String.valueOf(receipt.getProductAmount()));
        data.put("product-total", String.format("%.2f", receipt.getTotal()));
        data.put("total", String.format("%.2f", receipt.getTotal()));
        data.put("sender-email", "sharphurt@catstack.net");
        data.put("recipient-email", receipt.getBuyerEmail());
        data.put("kkt", String.valueOf(receipt.getCompany().getKkt()));
        data.put("fn", String.valueOf(receipt.getFiscalSign()));
        data.put("fd", String.valueOf(receipt.getId()));
        data.put("fp", String.valueOf(receipt.getFiscalAccumulator()));
        return data;
    }

    private String insertDataToHTML(HashMap<String, String> data, String html) {
        var result = html;
        for (String key : data.keySet()) {
            result = result.replaceAll("%" + key + "%", data.get(key));
        }
        return result;
    }

}
