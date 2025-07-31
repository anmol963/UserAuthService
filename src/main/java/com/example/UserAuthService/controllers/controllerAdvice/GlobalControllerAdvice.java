package com.example.UserAuthService.controllers.controllerAdvice;

import com.example.UserAuthService.dtos.ErrorResponseDto;
import com.example.UserAuthService.exceptions.InvalidTokenException;
import com.example.UserAuthService.exceptions.UnauthorisedException;
import com.example.UserAuthService.exceptions.UserNotFoundException;
import com.example.UserAuthService.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(e.getMessage());
        errorResponseDto.setStatusCode(409); // conflict
        errorResponseDto.setTimeStamp(new Date());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(e.getMessage());
        errorResponseDto.setStatusCode(404); // not found
        errorResponseDto.setTimeStamp(new Date());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UnauthorisedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnauthorisedException(UnauthorisedException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(e.getMessage());
        errorResponseDto.setStatusCode(401);
        errorResponseDto.setTimeStamp(new Date());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidTokenException(InvalidTokenException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(e.getMessage());
        errorResponseDto.setStatusCode(404);
        errorResponseDto.setTimeStamp(new Date());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }
}
