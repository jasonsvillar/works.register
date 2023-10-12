package com.jasonvillar.works.register.services;

import com.jasonvillar.works.register.entities.Privilege;
import com.jasonvillar.works.register.repositories.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrivilegeService {
    private final PrivilegeRepository privilegeRepository;

    public List<Privilege> getList() {
        return privilegeRepository.findAll();
    }

    public Privilege getById(long id) {
        return privilegeRepository.findPrivilegeById(id);
    }

    public Optional<Privilege> getOptionalById(long id) {
        return privilegeRepository.findOptionalById(id);
    }

    public List<Privilege> getListByNameLike(String name) {
        return privilegeRepository.findAllByNameContainingIgnoreCase(name);
    }
}