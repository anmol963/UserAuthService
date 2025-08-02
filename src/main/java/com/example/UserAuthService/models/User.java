package com.example.UserAuthService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "users")
public class User extends BaseModel{

    private String name;

    private String email;

    private String password;

    private String phoneNumber;

    @ManyToMany
    private List<Role> roles = new ArrayList<>();
}
