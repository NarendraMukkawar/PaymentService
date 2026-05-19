package com.paymenttransactionservice.service;

import com.paymenttransactionservice.dto.AccountRequestDTO;
import com.paymenttransactionservice.dto.TransferRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String ACCOUNT_SERVICE_URL = "http://localhost:8082/api/accounts";

    @Override
    public void transfer(TransferRequestDTO request) {

        boolean debitSuccess = false;

        try {
            // Create Debit Request
            AccountRequestDTO debitRequest = new AccountRequestDTO();
            debitRequest.setAccountId(request.getFromAccountId());
            debitRequest.setAmount(request.getAmount());

            // Debit
            restTemplate.postForObject(ACCOUNT_SERVICE_URL + "/debit", debitRequest, String.class);

            debitSuccess = true;

            // Create Credit Request
            AccountRequestDTO creditRequest = new AccountRequestDTO();
            creditRequest.setAccountId(request.getToAccountId());
            creditRequest.setAmount(request.getAmount());

            // Credit
            restTemplate.postForObject(ACCOUNT_SERVICE_URL + "/credit", creditRequest, String.class);

        } catch (Exception ex) {

            // Compensation (only if debit happened)
            if (debitSuccess) {
                AccountRequestDTO rollbackRequest = new AccountRequestDTO();
                rollbackRequest.setAccountId(request.getFromAccountId());
                rollbackRequest.setAmount(request.getAmount());

                restTemplate.postForObject(ACCOUNT_SERVICE_URL + "/credit", rollbackRequest, String.class);
            }

            throw new RuntimeException("Transaction failed, rolled back");
        }
    }
}