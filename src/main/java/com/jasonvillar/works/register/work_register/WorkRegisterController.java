package com.jasonvillar.works.register.work_register;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/v1/works-registers")
@Tag(name = "work", description = "the work API tag annotation")
public class WorkRegisterController {
    private final WorkRegisterService service;

    private final UserService userService;

    private final ServiceService serviceService;

    private final ClientService clientService;

    private final WorkRegisterRequestAdapter workRegisterRequestAdapter;

    private final WorkRegisterDTOAdapter workRegisterDTOAdapter;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<WorkRegisterDTO> getWorkRegister(@PathVariable long id) {
        Optional<WorkRegister> optional = this.service.getOptionalById(id);
        if (optional.isPresent()) {
            WorkRegisterDTO dto = workRegisterDTOAdapter.apply(optional.get());
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<WorkRegisterDTO>> getListWorkRegister() {
        List<WorkRegisterDTO> listDTO = this.service.getList().stream().map(workRegisterDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/user-id/{userId}", produces = "application/json")
    public ResponseEntity<List<WorkRegisterDTO>> getListWorkRegisterByUserId(@PathVariable long userId) {
        List<WorkRegisterDTO> listDTO = this.service.getListByUserId(userId).stream().map(workRegisterDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/client-id/{clientId}", produces = "application/json")
    public ResponseEntity<List<WorkRegisterDTO>> getListWorkRegisterByClientId(@PathVariable long clientId) {
        List<WorkRegisterDTO> listDTO = this.service.getListByClientId(clientId).stream().map(workRegisterDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/user-id/{userId}/client-id/{clientId}", produces = "application/json")
    public ResponseEntity<List<WorkRegisterDTO>> getListWorkRegisterByUserIdAndClientId(@PathVariable long userId, @PathVariable long clientId) {
        List<WorkRegisterDTO> listDTO = this.service.getListByUserIdAndClientId(userId, clientId).stream().map(workRegisterDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/title-like/{title}", produces = "application/json")
    public ResponseEntity<List<WorkRegisterDTO>> getListWorkRegisterByTitleLike(@PathVariable String title) {
        List<WorkRegisterDTO> listDTO = this.service.getListByTitleLike(title).stream().map(workRegisterDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveWorkRegister(@Valid @RequestBody WorkRegisterRequest request) {
        WorkRegister entity = this.workRegisterRequestAdapter.toEntity(request);
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