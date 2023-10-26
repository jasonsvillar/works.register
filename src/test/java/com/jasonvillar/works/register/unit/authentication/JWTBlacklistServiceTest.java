package com.jasonvillar.works.register.unit.authentication;

import com.jasonvillar.works.register.authentication.JWTBlacklist;
import com.jasonvillar.works.register.authentication.JWTBlacklistRepository;
import com.jasonvillar.works.register.authentication.JWTBlacklistService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class JWTBlacklistServiceTest {
    @Mock
    private JWTBlacklistRepository repository;

    @InjectMocks
    private JWTBlacklistService service;

    private JWTBlacklist entity = JWTBlacklist.builder()
            .token("token")
            .dateExpire(new Date())
            .build();

    @Test
    void givenRepositories_whenGetList_thenReturnList() {
        Mockito.when(repository.findOptionalByToken("token")).thenReturn(Optional.of(entity));

        Optional<JWTBlacklist> optional = service.getOptionalByToken("token");

        Assertions.assertThat(optional).isPresent();
    }

    @Test
    void givenRepositories_whenSave_thenReturnEntity() {
        Mockito.when(repository.save(entity)).thenReturn(entity);

        JWTBlacklist entitySaved = service.save(entity);

        Assertions.assertThat(entity).isEqualTo(entitySaved);

        service.deleteByToken("token");
    }

    @Test
    void givenRepositories_whenIsInBlacklist_thenReturnTrue() {
        Mockito.when(repository.findOptionalByToken("token")).thenReturn(Optional.of(entity));

        boolean isInBlacklist = service.isInBlacklist("token");
        Assertions.assertThat(isInBlacklist).isTrue();
    }
}
