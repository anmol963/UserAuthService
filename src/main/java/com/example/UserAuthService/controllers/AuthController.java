package com.example.UserAuthService.controllers;

import com.example.UserAuthService.dtos.*;
import com.example.UserAuthService.exceptions.*;
import com.example.UserAuthService.models.Token;
import com.example.UserAuthService.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto signupRequestDto) throws UserAlreadyExistsException {
        return new ResponseEntity<>(this.authService.signup(signupRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginRequestDto loginRequestDto) throws UserNotFoundException, UnauthorisedException {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @GetMapping("/validate")
    public ResponseEntity<UserDto> validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto) throws InvalidTokenException, TokenExpiredException {
        return ResponseEntity.ok(authService.validateToken(validateTokenRequestDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logOut(@RequestBody LogOutRequestDto logOutRequestDto) throws InvalidTokenException {
        authService.logout(logOutRequestDto);
        return ResponseEntity.ok("Logout Successful");
    }

    @PostMapping("/user/update/{id}")
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto, @PathVariable("id") Long id) throws UserNotFoundException {
        return ResponseEntity.ok(authService.update(userDto, id));
    }
}
