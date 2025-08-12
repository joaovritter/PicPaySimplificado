package com.joaozao.picpaysimplificado.dto;

import com.joaozao.picpaysimplificado.domain.UserType;

import java.math.BigDecimal;

public record UserDTO(
        String firstName,
        String lastName,
        String document,
        BigDecimal balance,
        UserType userType,
        String email,
        String password

    ){
}
