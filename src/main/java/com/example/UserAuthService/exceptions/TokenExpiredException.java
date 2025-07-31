package com.example.UserAuthService.exceptions;

public class TokenExpiredException extends Exception{

    public TokenExpiredException(String msg) {
        super(msg);
    }
}
