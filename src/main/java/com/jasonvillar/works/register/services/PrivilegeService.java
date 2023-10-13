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
        return this.privilegeRepository.findAll();
    }

    public Privilege getById(long id) {
        return this.privilegeRepository.findPrivilegeById(id);
    }

    public Optional<Privilege> getOptionalById(long id) {
        return this.privilegeRepository.findOptionalById(id);
    }

    public List<Privilege> getListByNameLike(String name) {
        return this.privilegeRepository.findAllByNameContainingIgnoreCase(name);
    }

    //TODO: feature to getPrivilegeOfUser
//    public List<Privilege> getAllPrivilegeByUserId(long userId) {
//        return this.privilegeRepository.findAllByUser
//    }
}