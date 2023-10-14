package com.jasonvillar.works.register.authentication;

import com.jasonvillar.works.register.authentication.Privilege;
import com.jasonvillar.works.register.authentication.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Privilege> getListByUserId(long userId) {
        return this.privilegeRepository.findAllDistinctByInRoleListInUserListId(userId);
    }

    public List<SimpleGrantedAuthority> getSimpleGrantedAuthorityList(long userId) {
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();
        this.getListByUserId(userId).forEach(
                privilege -> simpleGrantedAuthorityList.add(
                        new SimpleGrantedAuthority( privilege.getName() )
                )
        );

        return simpleGrantedAuthorityList;
    }
}