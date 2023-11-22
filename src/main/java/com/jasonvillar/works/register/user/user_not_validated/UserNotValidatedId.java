package com.jasonvillar.works.register.user.user_not_validated;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserNotValidatedId implements Serializable {
    private String name;
    private String email;

    @Builder
    public UserNotValidatedId(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
