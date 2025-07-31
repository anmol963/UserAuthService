package com.example.UserAuthService.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErrorResponseDto {

    private String message;

    private int statusCode;

    private Date timeStamp;
}
