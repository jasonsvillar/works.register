package com.jasonvillar.works.register.services.security;

import com.jasonvillar.works.register.configs.security.SecurityUser;
import com.jasonvillar.works.register.entities.User;
import com.jasonvillar.works.register.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = this.userService.getOptionalByName(username);

        if (userOptional.isPresent()) {
            return new SecurityUser(userOptional.get());
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}
