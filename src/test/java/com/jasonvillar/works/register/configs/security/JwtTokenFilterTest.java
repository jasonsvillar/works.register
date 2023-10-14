package com.jasonvillar.works.register.configs.security;

import com.jasonvillar.works.register.security.JwtTokenFilter;
import com.jasonvillar.works.register.security.JwtTokenProvider;
import com.jasonvillar.works.register.security.SecurityUser;
import com.jasonvillar.works.register.user.UserController;
import com.jasonvillar.works.register.user.port.out.UserDTO;
import com.jasonvillar.works.register.user.port.out.UserDTOMapper;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user.UserService;
import com.jasonvillar.works.register.authentication.SecurityUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class JwtTokenFilterTest {
    @MockBean
    private UserService userService = Mockito.mock(UserService.class);

    @MockBean
    private UserDTOMapper userDTOMapper = Mockito.mock(UserDTOMapper.class);

    @MockBean
    private SecurityUserDetailsService securityUserDetailsService = Mockito.mock(SecurityUserDetailsService.class);

    @MockBean
    private JwtTokenProvider jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);

    @Test
    void givenRequest_whenGetUsers_thenStatusResponseIsOk() throws Exception {
        Mockito.when(jwtTokenProvider.resolveToken(Mockito.any(HttpServletRequest.class))).thenReturn("randomBearerToken");
        Mockito.when(jwtTokenProvider.validateToken("randomBearerToken")).thenReturn(true);
        Mockito.when(jwtTokenProvider.getUsername("randomBearerToken")).thenReturn("extractedUserName");
        Mockito.when(securityUserDetailsService.loadUserByUsername("extractedUserName")).thenReturn(
                new SecurityUser(User.builder().name("extractedUserName").build())
        );
        List<User> listUser = List.of(User.builder().build());
        Mockito.when(userService.getList()).thenReturn(listUser);
        Mockito.when(userDTOMapper.apply(
                User.builder().name("extractedUserName").build()
        )).thenReturn(new UserDTO(1, "extractedUserName", "test@test.com"));

        standaloneSetup(new UserController(userService, userDTOMapper))
                .addFilters(new JwtTokenFilter(jwtTokenProvider, securityUserDetailsService)).build()
                .perform(
                        get("/api/v1/users").header("Authorization", "Bearer randomBearerToken")
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.when(jwtTokenProvider.validateToken(Mockito.anyString())).thenReturn(false);

        standaloneSetup(new UserController(userService, userDTOMapper))
                .addFilters(new JwtTokenFilter(jwtTokenProvider, securityUserDetailsService)).build()
                .perform(
                        get("/api/v1/users")
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
