package com.example.UserAuthService.services;

import com.example.UserAuthService.dtos.*;
import com.example.UserAuthService.exceptions.*;
import com.example.UserAuthService.models.Token;
import com.example.UserAuthService.models.User;
import com.example.UserAuthService.repository.TokenRepository;
import com.example.UserAuthService.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{

    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private ModelMapper modelMapper;

    private TokenRepository tokenRepository;

    public AuthServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           ModelMapper modelMapper,
                           TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public UserDto signup(SignupRequestDto signupRequestDto) throws UserAlreadyExistsException{
        Optional<User> optionalUser = userRepository.findByEmail(signupRequestDto.getEmail());
        if(optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("Email already registered. Proceed with Login.");
        }

        User user = new User();
        user.setName(signupRequestDto.getName());
        user.setEmail(signupRequestDto.getEmail());
        user.setPhoneNumber(signupRequestDto.getPhoneNumber());
        user.setPassword(bCryptPasswordEncoder.encode(signupRequestDto.getPassword()));

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public Token login(LoginRequestDto loginRequestDto) throws UserNotFoundException, UnauthorisedException {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequestDto.getEmail());
        // check if email exists in DB
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with email " + loginRequestDto.getEmail() + " not found");
        }
        User user = optionalUser.get();
        // validate the password
        if(!bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new UnauthorisedException("Incorrect Password");
        }
        // generate token
        Token token = new Token();
        token.setUser(user);
        token.setCreatedAt(LocalDateTime.now());
        token.setDeleted(false);
        token.setExpiryAt(LocalDateTime.now().plusDays(30));
        token.setValue(RandomStringUtils.secure().nextPrint(30));
        return tokenRepository.save(token);
    }

    @Override
    public UserDto validateToken(ValidateTokenRequestDto validateTokenRequestDto) throws InvalidTokenException, TokenExpiredException {
        // check if token exists, not deleted and not expired
        Optional<Token> optionalToken = tokenRepository.findByValueAndAndIsDeletedAndExpiryAtGreaterThan(
                validateTokenRequestDto.getToken(),
                false,
                LocalDateTime.now()
        );
        // check if token exists in DB
        if(optionalToken.isEmpty()) {
            throw new InvalidTokenException("Invalid token");
        }
        Token token = optionalToken.get();
        User user = token.getUser();
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public void logout(LogOutRequestDto logOutRequestDto) throws InvalidTokenException {
        Optional<Token> optionalToken = tokenRepository.findByValue(logOutRequestDto.getTokenValue());
        if(optionalToken.isEmpty()) {
            throw new InvalidTokenException("Invalid token");
        }
        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }
}
