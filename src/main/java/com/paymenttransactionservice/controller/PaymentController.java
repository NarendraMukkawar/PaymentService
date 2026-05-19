package com.paymenttransactionservice.controller;

import com.paymenttransactionservice.dto.TransferRequestDTO;
import com.paymenttransactionservice.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequestDTO request) {
        service.transfer(request);
        return "Transfer successful";
    }
}