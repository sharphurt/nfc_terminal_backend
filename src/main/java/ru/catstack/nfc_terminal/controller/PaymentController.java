package ru.catstack.nfc_terminal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.catstack.nfc_terminal.model.payload.request.CreatePaymentRequest;
import ru.catstack.nfc_terminal.model.payload.response.ApiResponse;
import ru.catstack.nfc_terminal.service.PaymentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/payment/")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ApiResponse authenticateUser(@Valid @RequestBody CreatePaymentRequest request) {
        var response = paymentService.acceptPayment(request);
        return new ApiResponse(response);
    }
}

