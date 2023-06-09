package com.jasonvillar.works.register.controllers;

import com.jasonvillar.works.register.dto.service.ServiceDTO;
import com.jasonvillar.works.register.dto.service.ServiceMapper;
import com.jasonvillar.works.register.dto.service.ServiceRequest;
import com.jasonvillar.works.register.entities.Service;
import com.jasonvillar.works.register.services.ServiceService;
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
@RequestMapping("api/v1/services")
public class ServiceController {
    private final ServiceService service;

    private final ServiceMapper mapper;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ServiceDTO> getService(@PathVariable long id) {
        Optional<Service> optional = this.service.getOptionalById(id);
        if (optional.isPresent()) {
            ServiceDTO dto = mapper.apply(optional.get());
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ServiceDTO>> getListService() {
        List<ServiceDTO> listDTO = this.service.getList().stream().map(mapper).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/name-like/{name}", produces = "application/json")
    public ResponseEntity<List<ServiceDTO>> getListServiceByNameLike(@PathVariable String name) {
        List<ServiceDTO> listDTO = this.service.getListByNameLike(name).stream().map(mapper).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveService(@Valid @RequestBody ServiceRequest request) {
        Service entity = this.mapper.toEntity(request);
        String message = this.service.getValidationsMessageWhenCantBeSaved(entity);

        if (message.isEmpty()) {
            entity = this.service.save(entity);
            ServiceDTO dto = this.mapper.apply(entity);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }
}
