package com.jasonvillar.works.register.services;

import com.jasonvillar.works.register.entities.User;
import com.jasonvillar.works.register.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
                message.append("\n");
            }
            message.append("email must be unique");
        }

        return message.toString();
    }

    public String plainPasswordToBcrypt(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public User save(User user) {
        String password = this.plainPasswordToBcrypt(user.getPassword());
        user.setPassword(password);
        return this.userRepository.save(user);
    }
}
