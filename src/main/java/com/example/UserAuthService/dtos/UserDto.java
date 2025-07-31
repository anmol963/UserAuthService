package com.example.UserAuthService.dtos;

import com.example.UserAuthService.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private List<Role> roles;
}
