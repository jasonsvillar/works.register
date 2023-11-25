package com.jasonvillar.works.register.user;

import com.jasonvillar.works.register.authentication.Role;
import com.jasonvillar.works.register.authentication.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    public List<User> getList() {
        return this.userRepository.findAll();
    }

    public List<User> getListByNameLike(String name) {
        return this.userRepository.findAllByNameContainingIgnoreCase(name);
    }

    public List<User> getListByEmailLike(String email) {
        return this.userRepository.findAllByEmailContainingIgnoreCase(email);
    }

    public List<User> getListByNameLikeAndEmailLike(String name, String email) {
        return this.userRepository.findAllByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(name, email);
    }

    public User getById(long id) {
        return this.userRepository.findUserById(id);
    }

    public Optional<User> getOptionalById(long id) {
        return this.userRepository.findOptionalById(id);
    }

    public Optional<User> getOptionalByName(String userName) {
        return this.userRepository.findOptionalByName(userName);
    }

    public Optional<User> getOptionalByEmail(String email) {
        return this.userRepository.findOptionalByEmail(email);
    }

    public boolean isExistName(String name) {
        return this.getOptionalByName(name).isPresent();
    }

    public boolean isExistEmail(String email) {
        return this.getOptionalByEmail(email).isPresent();
    }

    public boolean isExistId(long id) {
        return this.getOptionalById(id).isPresent();
    }

    public String getValidationsMessageWhenCantBeSaved(User entity) {
        StringBuilder message = new StringBuilder();

        if (this.isExistName(entity.getName())) {
            message.append("name must be unique");
        }

        if (this.isExistEmail(entity.getEmail())) {
            if (!message.isEmpty()) {
                message.append(", ");
            }
            message.append("email must be unique");
        }

        return message.toString();
    }

    public String plainPasswordToBcrypt(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Transactional
    public boolean addAdminRoleToUserById(long id) {
        User user = this.getById(id);
        Role roleAdmin = this.roleService.getById(1);
        Set<Role> roleList = this.roleService.getListByUserId(id);

        user.setHasRoleList(roleList);
        user.addRole(roleAdmin);
        return true;
    }

    public Optional<User> getOptionalByNameAndEmail(String name, String email) {
        return this.userRepository.findOptionalByNameAndEmail(name, email);
    }

    public User getReferenceById(long userId) {
        return this.userRepository.getReferenceById(userId);
    }
}
