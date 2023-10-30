package com.jasonvillar.works.register.authentication.port.out;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record AuthenticationResponse(long id,
                                     String name,
                                     String email,
                                     Collection<? extends GrantedAuthority> authorityList) {
}
