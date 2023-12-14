package com.jasonvillar.works.register.client;

import com.jasonvillar.works.register.authentication.SecurityUserDetailsService;
import com.jasonvillar.works.register.client.port.in.ClientRequestAdapter;
import com.jasonvillar.works.register.client.port.out.ClientDTO;
import com.jasonvillar.works.register.client.port.out.ClientDTOAdapter;
import com.jasonvillar.works.register.client.port.in.ClientRequest;
import com.jasonvillar.works.register.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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
@Tag(name = "4 - Client", description = "the client API for user logged")
public class ClientController {
    private final ClientService service;

    private final ClientRequestAdapter clientRequestAdapter;

    private final ClientDTOAdapter clientDTOAdapter;

    private final SecurityUserDetailsService securityUserDetailsService;

    private final com.jasonvillar.works.register.user.UserService userService;

    @GetMapping(value = "/clients/page/{page}/rows/{rows}", produces = "application/json")
    public ResponseEntity<List<ClientDTO>> getListClient(@AuthenticationPrincipal UserDetails userDetails,
                                                          @PathVariable int page,
                                                          @PathVariable int rows,
                                                          @RequestParam(required = false) Long id,
                                                          @RequestParam(required = false) String name,
                                                          @RequestParam(required = false) String surname,
                                                          @RequestParam(required = false) String identificationNumber) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);

        Specification<Client> specifications = this.service.makeSpecification(userId, id, name, surname, identificationNumber);

        List<ClientDTO> listDTO = this.service.getListBySpecificationAndPage(specifications, page - 1, rows).stream().map(clientDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/clients/row-count", produces = "application/json")
    public ResponseEntity<Long> getRowCount(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestParam(required = false) Long id,
                                            @RequestParam(required = false) String name,
                                            @RequestParam(required = false) String surname,
                                            @RequestParam(required = false) String identificationNumber) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);

        Specification<Client> specifications = this.service.makeSpecification(userId, id, name, surname, identificationNumber);

        long rowCount = this.service.getRowCountBySpecification(specifications);

        return ResponseEntity.ok().body(rowCount);
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
        List<ClientDTO> listDTO = this.service.getListByIdentificationNumberLikeAndUserId(dni, userId).stream().map(clientDTOAdapter).toList();

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
        User user = this.userService.getById(userId);

        Client entity = this.clientRequestAdapter.toEntity(request);
        String message = this.service.getValidationsMessageWhenCantBeSaved(entity, userId);

        if (message.isEmpty()) {
            entity.setUser(user);
            entity = this.service.save(entity);
            ClientDTO dto = this.clientDTOAdapter.apply(entity);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    @DeleteMapping(value = "/client/{id}")
    public ResponseEntity<Boolean> deleteClientToUserId(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);

        boolean deleted = this.service.deleteByClientIdAndUserId(id, userId);
        if (deleted) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/clients/delete")
    public ResponseEntity<List<ClientDTO>> deleteClientsBatchToUserId(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody List<Long> clientIdLongList) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);

        List<Client> clientThatCanBeDeleted = this.service.getListByIdListAndUserIdAndNotInWorkRegister(clientIdLongList, userId);

        this.service.deleteByClientList(clientThatCanBeDeleted);

        List<ClientDTO> clientListDTO = clientThatCanBeDeleted.stream().map(this.clientDTOAdapter).toList();

        return new ResponseEntity<>(clientListDTO, HttpStatus.OK);
    }
}
