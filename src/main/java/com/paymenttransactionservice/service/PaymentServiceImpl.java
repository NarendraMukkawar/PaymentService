package com.paymenttransactionservice.service;

import com.paymenttransactionservice.dto.AccountRequestDTO;
import com.paymenttransactionservice.dto.TransferRequestDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final RestTemplate restTemplate;

    private static final String ACCOUNT_SERVICE_URL = "http://localhost:8082/api/accounts";

    public PaymentServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void transfer(TransferRequestDTO request) {

        boolean debitSuccess = false;

        try {
            // Create Debit Request
//            AccountRequestDTO debitRequest = new AccountRequestDTO();
//            debitRequest.setAccountId(request.getFromAccountId());
//            debitRequest.setAmount(request.getAmount());

            // Debit
            //restTemplate.postForObject(ACCOUNT_SERVICE_URL + "/" + request.getToAccountId() + "/debit", debitRequest, String.class);

            String debitUrl = ACCOUNT_SERVICE_URL + "/" + request.getFromAccountNumber() + "/debit?amount=" + request.getAmount();

            restTemplate.exchange(debitUrl, HttpMethod.PATCH, HttpEntity.EMPTY, String.class);
            debitSuccess = true;

            // Create Credit Request
//            AccountRequestDTO creditRequest = new AccountRequestDTO();
//            creditRequest.setAccountId(request.getToAccountId());
//            creditRequest.setAmount(request.getAmount());
//
//            // Credit
//            restTemplate.postForObject(ACCOUNT_SERVICE_URL + "/credit", creditRequest, String.class);

            String creditUrl = ACCOUNT_SERVICE_URL + "/" + request.getToAccountNumber() + "/credit?amount=" + request.getAmount();

            restTemplate.exchange(creditUrl, HttpMethod.PATCH, HttpEntity.EMPTY, String.class);

        } catch (Exception ex) {

            // Compensation (only if debit happened)
//            if (debitSuccess) {
//                AccountRequestDTO rollbackRequest = new AccountRequestDTO();
//                rollbackRequest.setAccountId(request.getFromAccountId());
//                rollbackRequest.setAmount(request.getAmount());
//
//                restTemplate.postForObject(ACCOUNT_SERVICE_URL + "/credit", rollbackRequest, String.class);
//            }
            if (debitSuccess) {

                String rollbackUrl = ACCOUNT_SERVICE_URL + "/" + request.getFromAccountNumber() + "/credit?amount=" + request.getAmount();

                restTemplate.exchange(rollbackUrl, HttpMethod.PATCH, HttpEntity.EMPTY, String.class);
            }

            throw new RuntimeException("Transaction failed: " + ex.getMessage());
        }
    }
}