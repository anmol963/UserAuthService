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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
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
        return UserDto.from(savedUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        token.setDeleted(false);
        token.setExpiryAt(LocalDateTime.now().plusDays(30));
        token.setValue(RandomStringUtils.secure().nextPrint(100));
        return tokenRepository.save(token);
    }

    @Override
    public UserDto validateToken(ValidateTokenRequestDto validateTokenRequestDto) throws InvalidTokenException, TokenExpiredException {
        // check if token exists, not deleted and not expired
        Optional<Token> optionalToken = tokenRepository.findByValueAndIsDeletedAndExpiryAtGreaterThan(
                validateTokenRequestDto.getToken(),
                false,
                LocalDateTime.now()
        );
        if(optionalToken.isEmpty()) {
            throw new InvalidTokenException("Invalid token");
        }
        Token token = optionalToken.get();
        User user = token.getUser();
        return UserDto.from(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logout(LogOutRequestDto logOutRequestDto) throws InvalidTokenException {
        Optional<Token> optionalToken = tokenRepository.findByValueAndIsDeletedAndExpiryAtGreaterThan(
                logOutRequestDto.getTokenValue(),
                false,
                LocalDateTime.now());
        if(optionalToken.isEmpty()) {
            throw new InvalidTokenException("Invalid token");
        }
        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto update(UserDto userDto, Long id) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        User user = optionalUser.get();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        userRepository.save(user);
        return userDto;
    }
}
