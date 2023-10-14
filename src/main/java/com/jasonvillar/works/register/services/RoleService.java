package com.jasonvillar.works.register.services;

import com.jasonvillar.works.register.entities.Role;
import com.jasonvillar.works.register.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> getList() {
        return roleRepository.findAll();
    }

    public Role getById(long id) {
        return roleRepository.findRoleById(id);
    }

    public Optional<Role> getOptionalById(long id) {
        return roleRepository.findOptionalById(id);
    }

    public List<Role> getListByNameLike(String name) {
        return roleRepository.findAllByNameContainingIgnoreCase(name);
    }

    public Set<Role> getListByUserId(long id) {
        return roleRepository.findAllByInUserListId(id);
    }
}