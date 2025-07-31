package com.example.UserAuthService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "tokens")
public class Token extends BaseModel{

    private String value;

    private boolean isDeleted;

    private LocalDateTime expiryAt;

    @ManyToOne
    private User user;
}
