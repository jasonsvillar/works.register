package com.jasonvillar.works.register.controllers;

import com.jasonvillar.works.register.dto.user.UserMapper;
import com.jasonvillar.works.register.dto.user.UserRequest;
import com.jasonvillar.works.register.dto.user.UserDTO;
import com.jasonvillar.works.register.entities.User;
import com.jasonvillar.works.register.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService service;

    private final UserMapper mapper;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        Optional<User> optional = this.service.getOptionalById(id);
        if (optional.isPresent()) {
            UserDTO dto = mapper.apply(optional.get());
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<UserDTO>> getListUser() {
        List<UserDTO> listDTO = this.service.getList().stream().map(mapper).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/name-like/{name}", produces = "application/json")
    public ResponseEntity<List<UserDTO>> getListUserByNameLike(@PathVariable String name) {
        List<UserDTO> listDTO = this.service.getListByNameLike(name).stream().map(mapper).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/email-like/{email}", produces = "application/json")
    public ResponseEntity<List<UserDTO>> getListUserByEmailLike(@PathVariable String email) {
        List<UserDTO> listDTO = this.service.getListByEmailLike(email).stream().map(mapper).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/name-like/{name}/email-like/{email}", produces = "application/json")
    public ResponseEntity<List<UserDTO>> getListUserByNameLikeAndEmailLike(@PathVariable String name, @PathVariable String email) {
        List<UserDTO> listDTO = this.service.getListByNameLikeAndEmailLike(name, email).stream().map(mapper).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@Valid @RequestBody UserRequest request) {
        User entity = this.mapper.toEntity(request);
        String message = this.service.getValidationsMessageWhenCantBeSaved(entity);

        if (message.isEmpty()) {
            entity = this.service.save(entity);
            UserDTO dto = this.mapper.apply(entity);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }
}
