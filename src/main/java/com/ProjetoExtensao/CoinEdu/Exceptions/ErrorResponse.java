package com.ProjetoExtensao.CoinEdu.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
}
