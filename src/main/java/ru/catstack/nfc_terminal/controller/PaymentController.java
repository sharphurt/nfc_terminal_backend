package ru.catstack.nfc_terminal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.catstack.nfc_terminal.model.payload.request.CreatePaymentRequest;
import ru.catstack.nfc_terminal.model.payload.request.ReturnPaymentRequest;
import ru.catstack.nfc_terminal.model.payload.response.ApiResponse;
import ru.catstack.nfc_terminal.service.PaymentService;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/payment/")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ApiResponse createPayment(@Valid @RequestBody CreatePaymentRequest request) throws IOException {
        var response = paymentService.acceptPayment(request);
        return new ApiResponse(response);
    }

    @PostMapping("/return")
    public ApiResponse returnPayment(@Valid @RequestBody ReturnPaymentRequest request) throws IOException {
        var response = paymentService.returnPayment(request);
        return new ApiResponse(response);
    }
}

