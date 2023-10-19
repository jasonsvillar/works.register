package com.jasonvillar.works.register.client;

import com.jasonvillar.works.register.authentication.SecurityUserDetailsService;
import com.jasonvillar.works.register.client.port.in.ClientRequestAdapter;
import com.jasonvillar.works.register.client.port.out.ClientDTO;
import com.jasonvillar.works.register.client.port.out.ClientDTOAdapter;
import com.jasonvillar.works.register.client.port.in.ClientRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "client", description = "the client API tag annotation")
public class ClientController {
    private final ClientService service;

    private final ClientRequestAdapter clientRequestAdapter;

    private final ClientDTOAdapter clientDTOAdapter;

    private final SecurityUserDetailsService securityUserDetailsService;

    @GetMapping(value = "/clients",produces = "application/json")
    public ResponseEntity<List<ClientDTO>> getListClient(@AuthenticationPrincipal UserDetails userDetails) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        List<ClientDTO> listDTO = this.service.getListByUserId(userId).stream().map(clientDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/client/{id}", produces = "application/json")
    public ResponseEntity<ClientDTO> getClientById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        Optional<Client> optional = this.service.getOptionalByIdAndUserId(id, userId);
        if (optional.isPresent()) {
            ClientDTO dto = clientDTOAdapter.apply(optional.get());
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/clients/name-like/{name}", produces = "application/json")
    public ResponseEntity<List<ClientDTO>> getListClientByNameLike(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String name) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        List<ClientDTO> listDTO = this.service.getListByNameLikeAndUserId(name, userId).stream().map(clientDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/clients/surname-like/{surname}", produces = "application/json")
    public ResponseEntity<List<ClientDTO>> getListClientBySurnameLike(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String surname) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        List<ClientDTO> listDTO = this.service.getListBySurnameLikeAndUserId(surname, userId).stream().map(clientDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/clients/dni-like/{dni}", produces = "application/json")
    public ResponseEntity<List<ClientDTO>> getListClientByDniLike(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String dni) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        List<ClientDTO> listDTO = this.service.getListByDniLikeAndUserId(dni, userId).stream().map(clientDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/clients/name-like/{name}/surname-like/{surname}", produces = "application/json")
    public ResponseEntity<List<ClientDTO>> getListClientByNameLikeAndSurnameLike(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String name, @PathVariable String surname) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        List<ClientDTO> listDTO = this.service.getListByNameLikeAndSurnameLikeAndUserId(name, surname, userId).stream().map(clientDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @PostMapping(value = "/client")
    public ResponseEntity<Object> saveClient(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody ClientRequest request) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        Client entity = this.clientRequestAdapter.toEntity(request);
        String message = this.service.getValidationsMessageWhenCantBeSaved(entity);

        if (message.isEmpty()) {
            entity = this.service.saveWithUser(entity, userId);
            ClientDTO dto = this.clientDTOAdapter.apply(entity);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            // TODO: get existent clientId and save with the current user
            return ResponseEntity.badRequest().body(message);
        }
    }
}
