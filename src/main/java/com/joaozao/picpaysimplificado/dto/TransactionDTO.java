package com.joaozao.picpaysimplificado.dto;

import java.math.BigDecimal;

public record TransactionDTO (
    BigDecimal amount,
    Long senderId,
    Long receiverId
){}

