package com.jasonvillar.works.register.client;

import com.jasonvillar.works.register.client.port.in.ClientRequestAdapter;
import com.jasonvillar.works.register.client.port.out.ClientDTO;
import com.jasonvillar.works.register.client.port.out.ClientDTOAdapter;
import com.jasonvillar.works.register.client.port.in.ClientRequest;
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
@RequestMapping("api/v1")
@Tag(name = "client", description = "the client API tag annotation")
public class ClientController {
    private final ClientService service;

    private final ClientRequestAdapter clientRequestAdapter;

    private final ClientDTOAdapter clientDTOAdapter;

    @GetMapping(value = "/clients",produces = "application/json")
    public ResponseEntity<List<ClientDTO>> getListClient() {
        List<ClientDTO> listDTO = this.service.getList().stream().map(clientDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/client/{id}", produces = "application/json")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable long id) {
        Optional<Client> optional = this.service.getOptionalById(id);
        if (optional.isPresent()) {
            ClientDTO dto = clientDTOAdapter.apply(optional.get());
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/clients/name-like/{name}", produces = "application/json")
    public ResponseEntity<List<ClientDTO>> getListClientByNameLike(@PathVariable String name) {
        List<ClientDTO> listDTO = this.service.getListByNameLike(name).stream().map(clientDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/clients/surname-like/{surname}", produces = "application/json")
    public ResponseEntity<List<ClientDTO>> getListClientBySurnameLike(@PathVariable String surname) {
        List<ClientDTO> listDTO = this.service.getListBySurnameLike(surname).stream().map(clientDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/clients/dni-like/{dni}", produces = "application/json")
    public ResponseEntity<List<ClientDTO>> getListClientByDniLike(@PathVariable String dni) {
        List<ClientDTO> listDTO = this.service.getListByDniLike(dni).stream().map(clientDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/clients/name-like/{name}/surname-like/{surname}", produces = "application/json")
    public ResponseEntity<List<ClientDTO>> getListClientByNameLikeAndSurnameLike(@PathVariable String name, @PathVariable String surname) {
        List<ClientDTO> listDTO = this.service.getListByNameLikeAndSurnameLike(name, surname).stream().map(clientDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @PostMapping(value = "/client")
    public ResponseEntity<Object> saveClient(@Valid @RequestBody ClientRequest request) {
        Client entity = this.clientRequestAdapter.toEntity(request);
        String message = this.service.getValidationsMessageWhenCantBeSaved(entity);

        if (message.isEmpty()) {
            entity = this.service.save(entity);
            ClientDTO dto = this.clientDTOAdapter.apply(entity);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }
}
