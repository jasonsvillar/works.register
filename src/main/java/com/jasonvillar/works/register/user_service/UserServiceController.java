package com.jasonvillar.works.register.user_service;

import com.jasonvillar.works.register.user_service.port.in.UserServiceRequestAdapter;
import com.jasonvillar.works.register.user_service.port.out.UserServiceDTO;
import com.jasonvillar.works.register.user_service.port.out.UserServiceDTOAdapter;
import com.jasonvillar.works.register.user_service.port.in.UserServiceRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("api/v1/users-services")
@Tag(name = "user - service", description = "the user - service API tag annotation")
public class UserServiceController {
    private final UserServiceService service;

    private final UserServiceRequestAdapter userServiceRequestAdapter;

    private final UserServiceDTOAdapter userServiceDTOAdapter;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UserServiceDTO> getUserService(@PathVariable long id) {
        Optional<UserService> optional = this.service.getOptionalById(id);

        if (optional.isPresent()) {
            UserServiceDTO dto = userServiceDTOAdapter.apply(optional.get());
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/user-id/{userId}", produces = "application/json")
    public ResponseEntity<List<UserServiceDTO>> getListUserServiceByUserId(@PathVariable long userId) {
        List<UserServiceDTO> listDTO = this.service.getListByUserId(userId).stream().map(userServiceDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<UserServiceDTO>> getListUserService() {
        List<UserServiceDTO> listDTO = this.service.getList().stream().map(userServiceDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveUserService(@Valid @RequestBody UserServiceRequest request) {
        UserService entity = this.userServiceRequestAdapter.toEntity(request);
        String message = this.service.getValidationsMessageWhenCantBeSaved(entity);
        if (message.isEmpty()) {
            entity = this.service.save(entity);
            UserServiceDTO dto = this.userServiceDTOAdapter.apply(entity);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }
}
