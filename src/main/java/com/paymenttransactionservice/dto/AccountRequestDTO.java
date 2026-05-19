package com.paymenttransactionservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AccountRequestDTO {

    // IMPORTANT
    private Long accountId;
    // IMPORTANT
    private BigDecimal amount;

}
