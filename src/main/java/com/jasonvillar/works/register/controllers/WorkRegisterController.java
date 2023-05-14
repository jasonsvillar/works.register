package com.jasonvillar.works.register.controllers;

import com.jasonvillar.works.register.dto.workregister.WorkRegisterDTO;
import com.jasonvillar.works.register.dto.workregister.WorkRegisterMapper;
import com.jasonvillar.works.register.dto.workregister.WorkRegisterRequest;
import com.jasonvillar.works.register.entities.WorkRegister;
import com.jasonvillar.works.register.services.ClientService;
import com.jasonvillar.works.register.services.UserService;
import com.jasonvillar.works.register.services.ServiceService;
import com.jasonvillar.works.register.services.WorkRegisterService;
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
@RequestMapping("api/v1/works-registers")
public class WorkRegisterController {
    private final WorkRegisterService service;

    private final UserService userService;

    private final ServiceService serviceService;

    private final ClientService clientService;

    private final WorkRegisterMapper mapper;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<WorkRegisterDTO> getWorkRegister(@PathVariable long id) {
        Optional<WorkRegister> optional = this.service.getOptionalById(id);
        if (optional.isPresent()) {
            WorkRegisterDTO dto = mapper.apply(optional.get());
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<WorkRegisterDTO>> getListWorkRegister() {
        List<WorkRegisterDTO> listDTO = this.service.getList().stream().map(mapper).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/user-id/{userId}", produces = "application/json")
    public ResponseEntity<List<WorkRegisterDTO>> getListWorkRegisterByUserId(@PathVariable long userId) {
        List<WorkRegisterDTO> listDTO = this.service.getListByUserId(userId).stream().map(mapper).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/client-id/{clientId}", produces = "application/json")
    public ResponseEntity<List<WorkRegisterDTO>> getListWorkRegisterByClientId(@PathVariable long clientId) {
        List<WorkRegisterDTO> listDTO = this.service.getListByClientId(clientId).stream().map(mapper).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/user-id/{userId}/client-id/{clientId}", produces = "application/json")
    public ResponseEntity<List<WorkRegisterDTO>> getListWorkRegisterByUserIdAndClientId(@PathVariable long userId, @PathVariable long clientId) {
        List<WorkRegisterDTO> listDTO = this.service.getListByUserIdAndClientId(userId, clientId).stream().map(mapper).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/title-like/{title}", produces = "application/json")
    public ResponseEntity<List<WorkRegisterDTO>> getListWorkRegisterByTitleLike(@PathVariable String title) {
        List<WorkRegisterDTO> listDTO = this.service.getListByTitleLike(title).stream().map(mapper).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveWorkRegister(@Valid @RequestBody WorkRegisterRequest request) {
        WorkRegister entity = this.mapper.toEntity(request);
        String message = this.service.getValidationsMessageWhenCantBeSaved(entity);

        if (message.isEmpty()) {
            entity = this.service.save(entity);
            entity.setUser(userService.getById(entity.getUserId()));
            entity.setService(serviceService.getById(entity.getServiceId()));
            entity.setClient(clientService.getById(entity.getClientId()));

            WorkRegisterDTO dto = this.mapper.apply(entity);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }
}
