package com.example.UserAuthService.dtos;

import com.example.UserAuthService.models.Role;
import com.example.UserAuthService.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;;

    private List<String> roles;

    public static UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        List<String> roles = new ArrayList<>();
        user.getRoles().forEach(role -> roles.add(role.getValue()));
        userDto.setRoles(roles);
        return userDto;
    }
}
