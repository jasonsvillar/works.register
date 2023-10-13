package com.jasonvillar.works.register.services;

import com.jasonvillar.works.register.repositories.RoleRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RoleRepository repository;

    @InjectMocks
    private RoleService service;
}
