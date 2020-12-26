package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import ru.catstack.nfc_terminal.model.Receipt;
import ru.catstack.nfc_terminal.model.payload.request.ClientCompanyRegistrationRequest;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendReceiptMail(Receipt receipt) throws IOException {
        ClassPathResource cpr = new ClassPathResource("email_template/template.html");
        byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
        var htmlCode = new String(bdata, StandardCharsets.UTF_8);
        var data = getDataFromReceipt(receipt);
        var html = insertDataToHTML(data, htmlCode);
        sendMail(data.get("recipient-email"), "Кассовый чек о покупке", "no-reply@catstack.net", html);
    }

    public void sendRegistrationMail(ClientCompanyRegistrationRequest request) throws IOException {
        ClassPathResource cpr = new ClassPathResource("email_template/clientRegistrationTemplate.html");
        byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
        var htmlCode = new String(bdata, StandardCharsets.UTF_8);
        var data = getDataFromRegistrationRequest(request);
        var html = insertDataToHTML(data, htmlCode);
        sendMail(data.get("recipient-email"), "Ваша заявка была одобрена!", "no-reply@catstack.net", html);
    }

    public void sendMail(String to, String subject, String from, String html) {
        var mimeMessage = javaMailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            helper.setText(html, true); // Use this or above line.
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(from);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, String> getDataFromRegistrationRequest(ClientCompanyRegistrationRequest request) {
        var data = new HashMap<String, String>();
        data.put("recipient-email", request.getClient().getEmail());
        data.put("login", request.getClient().getEmail());
        data.put("password", request.getClient().getPassword());
        return data;
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
        if (!receipt.getVendor().getPatronymic().equals(""))
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
        data.put("sender-email", receipt.getVendor().getEmail());
        data.put("recipient-email", receipt.getBuyerEmail());
        data.put("kkt", String.valueOf(receipt.getCompany().getKkt()));
        data.put("fn", String.valueOf(receipt.getCompany().getFiscalSign()));
        data.put("fd", String.valueOf(receipt.getId()));
        data.put("fp", String.valueOf(receipt.getCompany().getFiscalAccumulator()));
        return data;
    }

    private String insertDataToHTML(HashMap<String, String> data, String html) {
        var result = html;
        for (String key : data.keySet()) {
            result = result.replace("%" + key + "%", data.get(key));
        }
        return result;
    }

}
