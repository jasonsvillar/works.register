package com.jasonvillar.works.register.unit.authentication;

import com.jasonvillar.works.register.authentication.JWTBlacklistRepository;
import com.jasonvillar.works.register.unit.configs_for_tests.repositories.DataJpaTestTemplate;
import com.jasonvillar.works.register.authentication.JWTBlacklist;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;

class JWTBlacklistRepositoryTest extends DataJpaTestTemplate {
    @Autowired
    JWTBlacklistRepository repository;

    private JWTBlacklist jwtInDatabase = JWTBlacklist.builder()
            .token("Token-Test-as54d4fa64sd4")
            .dateExpire(new Date())
            .build();

    @BeforeEach
    void setUp() {
        this.repository.deleteAll();
        this.jwtInDatabase = this.repository.save(this.jwtInDatabase);
    }

    @Test
    void givenEntity_WhenSave_thenCheckIfNotNull() {
        JWTBlacklist jwt = JWTBlacklist.builder()
                .token("Token-Test")
                .dateExpire(new Date())
                .build();

        JWTBlacklist jwtSaved = this.repository.save(jwt);
        Assertions.assertThat(jwtSaved).isNotNull();
    }

    @Test
    void givenEntityInDatabase_WhenDeleteByToken_thenCheckIfNotPresent() {
        Optional<JWTBlacklist> optionalJwtInDatabase = this.repository.findOptionalByToken("Token-Test-as54d4fa64sd4");
        Assertions.assertThat(optionalJwtInDatabase).isPresent();

        this.repository.deleteByToken("Token-Test-as54d4fa64sd4");
        optionalJwtInDatabase = this.repository.findOptionalByToken("Token-Test-as54d4fa64sd4");
        Assertions.assertThat(optionalJwtInDatabase).isNotPresent();
    }

    @Test
    void givenEntityInDatabase_WhenFindByToken_thenCheckIfPresent() {
        Optional<JWTBlacklist> optionalJwtInDatabase = this.repository.findOptionalByToken("Token-Test-as54d4fa64sd4");
        Assertions.assertThat(optionalJwtInDatabase).isPresent();
    }

    @Test
    void givenEntityInDatabase_WhenDeleteByDateExpire_thenCheckIfEmpty() {
        List<JWTBlacklist> jwtInDatabaseList = this.repository.findAllByDateExpireBefore(new Date());
        Assertions.assertThat(jwtInDatabaseList).isNotEmpty();

        this.repository.deleteByDateExpireBefore(new Date());
        jwtInDatabaseList = this.repository.findAllByDateExpireBefore(new Date());
        Assertions.assertThat(jwtInDatabaseList).isEmpty();
    }

    @Test
    void givenEntityInDatabase_WhenFindByDateExpire_thenCheckIfNotEmpty() {
        List<JWTBlacklist> jwtInDatabaseList = this.repository.findAllByDateExpireBefore(new Date());
        Assertions.assertThat(jwtInDatabaseList).isNotEmpty();
    }
}
