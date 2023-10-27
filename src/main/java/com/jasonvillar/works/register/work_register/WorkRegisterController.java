package com.jasonvillar.works.register.work_register;

import com.jasonvillar.works.register.authentication.SecurityUserDetailsService;
import com.jasonvillar.works.register.work_register.port.in.WorkRegisterRequestAdapter;
import com.jasonvillar.works.register.work_register.port.out.WorkRegisterDTO;
import com.jasonvillar.works.register.work_register.port.out.WorkRegisterDTOAdapter;
import com.jasonvillar.works.register.work_register.port.in.WorkRegisterRequest;
import com.jasonvillar.works.register.client.ClientService;
import com.jasonvillar.works.register.user.UserService;
import com.jasonvillar.works.register.service.ServiceService;
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
@Tag(name = "5 - Work", description = "the work API tag annotation")
public class WorkRegisterController {
    private final WorkRegisterService service;

    private final UserService userService;

    private final ServiceService serviceService;

    private final ClientService clientService;

    private final WorkRegisterRequestAdapter workRegisterRequestAdapter;

    private final WorkRegisterDTOAdapter workRegisterDTOAdapter;

    private final SecurityUserDetailsService securityUserDetailsService;

    @GetMapping(value = "/work/{id}", produces = "application/json")
    public ResponseEntity<WorkRegisterDTO> getWorkRegister(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        Optional<WorkRegister> optional = this.service.getOptionalByIdAndUserId(id, userId);
        if (optional.isPresent()) {
            WorkRegisterDTO dto = workRegisterDTOAdapter.apply(optional.get());
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/works", produces = "application/json")
    public ResponseEntity<List<WorkRegisterDTO>> getListWorkRegister(@AuthenticationPrincipal UserDetails userDetails) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        List<WorkRegisterDTO> listDTO = this.service.getListByUserId(userId).stream().map(workRegisterDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/works/client-id/{clientId}", produces = "application/json")
    public ResponseEntity<List<WorkRegisterDTO>> getListWorkRegisterByClientId(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long clientId) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        List<WorkRegisterDTO> listDTO = this.service.getListByUserIdAndClientId(userId, clientId).stream().map(workRegisterDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/works/title/{title}", produces = "application/json")
    public ResponseEntity<List<WorkRegisterDTO>> getListWorkRegisterByTitleLike(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String title) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        List<WorkRegisterDTO> listDTO = this.service.getListByTitleLikeAndUserId(title, userId).stream().map(workRegisterDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @PostMapping(value = "/work")
    public ResponseEntity<Object> saveWorkRegister(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody WorkRegisterRequest request) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        WorkRegister entity = this.workRegisterRequestAdapter.toEntity(request);
        entity.setUserId(userId);
        String message = this.service.getValidationsMessageWhenCantBeSaved(entity);

        if (message.isEmpty()) {
            entity = this.service.save(entity);
            entity.setUser(userService.getById(entity.getUserId()));
            entity.setService(serviceService.getById(entity.getServiceId()));
            entity.setClient(clientService.getById(entity.getClientId()));

            WorkRegisterDTO dto = this.workRegisterDTOAdapter.apply(entity);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }
}
