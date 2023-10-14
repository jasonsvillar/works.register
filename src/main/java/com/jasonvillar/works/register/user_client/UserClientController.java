package com.jasonvillar.works.register.user_client;

import com.jasonvillar.works.register.user_client.port.in.UserClientRequestAdapter;
import com.jasonvillar.works.register.user_client.port.out.UserClientDTO;
import com.jasonvillar.works.register.user_client.port.out.UserClientDTOAdapter;
import com.jasonvillar.works.register.user_client.port.in.UserClientRequest;
import com.jasonvillar.works.register.client.ClientService;
import com.jasonvillar.works.register.user.UserService;
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
@RequestMapping("api/v1/users-clients")
@Tag(name = "user - client", description = "the user - client API tag annotation")
public class UserClientController {
    private final UserClientService service;

    private final UserService userService;

    private final ClientService clientService;

    private final UserClientRequestAdapter userClientRequestAdapter;

    private final UserClientDTOAdapter userClientDTOAdapter;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<UserClientDTO>> getListUserClient() {
        List<UserClientDTO> listDTO = this.service.getList().stream().map(userClientDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UserClientDTO> getUserClient(@PathVariable long id) {
        Optional<UserClient> optional = this.service.getOptionalById(id);
        if (optional.isPresent()) {
            UserClientDTO dto = userClientDTOAdapter.apply(optional.get());
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/user-id/{userId}", produces = "application/json")
    public ResponseEntity<List<UserClientDTO>> getListUserClientByUserId(@PathVariable long userId) {
        List<UserClientDTO> listDTO = this.service.getListByUserId(userId).stream().map(userClientDTOAdapter).toList();
        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveUserClient(@Valid @RequestBody UserClientRequest request) {
        UserClient entity = this.userClientRequestAdapter.toEntity(request);
        String message = this.service.getValidationsMessageWhenCantBeSaved(entity);

        if (message.isEmpty()) {
            entity = this.service.save(entity);
            entity.setUser(userService.getById(entity.getUserId()));
            entity.setClient(clientService.getById(entity.getClientId()));
            UserClientDTO dto = this.userClientDTOAdapter.apply(entity);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }
}