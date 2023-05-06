package com.jasonvillar.works.register.controllers;

import com.jasonvillar.works.register.dto.userservice.UserServiceDTO;
import com.jasonvillar.works.register.dto.userservice.UserServiceMapper;
import com.jasonvillar.works.register.dto.userservice.UserServiceRequest;
import com.jasonvillar.works.register.entities.UserService;
import com.jasonvillar.works.register.services.UserServiceService;
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
@RequestMapping("users-services")
public class UserServiceController {
    private final UserServiceService service;

    private final UserServiceMapper mapper;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UserServiceDTO> getUserService(@PathVariable long id) {
        Optional<UserService> optional = this.service.getOptionalById(id);

        if (optional.isPresent()) {
            UserServiceDTO dto = mapper.apply(optional.get());
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/user-id/{userId}", produces = "application/json")
    public ResponseEntity<List<UserServiceDTO>> getListUserServiceByUserId(@PathVariable long userId) {
        List<UserServiceDTO> listDTO = this.service.getListByUserId(userId).stream().map(mapper).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<UserServiceDTO>> getListUserService() {
        List<UserServiceDTO> listDTO = this.service.getList().stream().map(mapper).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveUserService(@Valid @RequestBody UserServiceRequest request) {
        UserService entity = this.mapper.toEntity(request);
        String message = this.service.getValidationsMessageWhenCantBeSaved(entity);
        if (message.isEmpty()) {
            entity = this.service.save(entity);
            UserServiceDTO dto = this.mapper.apply(entity);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }
}
