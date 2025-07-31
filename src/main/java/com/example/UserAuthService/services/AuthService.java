package com.example.UserAuthService.services;

import com.example.UserAuthService.dtos.*;
import com.example.UserAuthService.exceptions.*;
import com.example.UserAuthService.models.Token;

public interface AuthService {

    UserDto signup(SignupRequestDto signupRequestDto) throws UserAlreadyExistsException;

    Token login(LoginRequestDto loginRequestDto) throws UserNotFoundException, UnauthorisedException;

    UserDto validateToken(ValidateTokenRequestDto validateTokenRequestDto) throws InvalidTokenException, TokenExpiredException;

    void logout(LogOutRequestDto logOutRequestDto) throws InvalidTokenException;
}
