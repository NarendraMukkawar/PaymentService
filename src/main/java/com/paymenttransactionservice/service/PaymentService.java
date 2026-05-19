package com.paymenttransactionservice.service;

import com.paymenttransactionservice.dto.TransferRequestDTO;

public interface PaymentService {
    void transfer(TransferRequestDTO request);
}