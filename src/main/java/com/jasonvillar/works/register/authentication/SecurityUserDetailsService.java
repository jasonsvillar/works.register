package com.jasonvillar.works.register.authentication;

import com.jasonvillar.works.register.security.SecurityUser;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final PrivilegeService privilegeService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = this.userService.getOptionalByName(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setAuthorityList(this.privilegeService.getSimpleGrantedAuthorityList(user.getId()));
            return new SecurityUser(user);
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }

    public long getAuthenticatedUserId(UserDetails userDetails) {
        return ((SecurityUser) userDetails).getId();
    }
}
