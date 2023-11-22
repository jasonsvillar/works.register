package com.jasonvillar.works.register.user.user_not_validated;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class UserNotValidated {
    @EmbeddedId
    private UserNotValidatedId userNotValidatedId;

    private String password;

    private String code;

    @Builder
    public UserNotValidated(UserNotValidatedId userNotValidatedId, String password, String code) {
        this.userNotValidatedId = userNotValidatedId;
        this.password = password;
        this.code = code;
    }
}
