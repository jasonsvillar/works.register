package com.jasonvillar.works.register.authentication;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "jwt_blacklist")
@Setter
@Getter
@NoArgsConstructor
public class JWTBlacklist {
    @Id
    @Column(name = "token")
    String token;

    @Column(name = "date_expire")
    Date dateExpire;

    @Builder
    public JWTBlacklist(String token, Date dateExpire) {
        this.token = token;
        this.dateExpire = dateExpire;
    }
}
