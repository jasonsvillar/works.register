package com.jasonvillar.works.register.service;

import com.jasonvillar.works.register.authentication.SecurityUserDetailsService;
import com.jasonvillar.works.register.service.port.in.ServiceRequestAdapter;
import com.jasonvillar.works.register.service.port.out.ServiceDTO;
import com.jasonvillar.works.register.service.port.out.ServiceDTOAdapter;
import com.jasonvillar.works.register.service.port.in.ServiceRequest;
import com.jasonvillar.works.register.user.User;
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
@Tag(name = "5 - Service", description = "the service API for user logged")
public class ServiceController {
    private final ServiceService service;

    private final ServiceDTOAdapter serviceDTOAdapter;

    private final ServiceRequestAdapter serviceRequestAdapter;

    private final SecurityUserDetailsService securityUserDetailsService;

    private final com.jasonvillar.works.register.user.UserService userService;

    @GetMapping(value = "/service/{id}", produces = "application/json")
    public ResponseEntity<ServiceDTO> getService(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        Optional<Service> optional = this.service.getOptionalByIdAndUserId(id, userId);
        if (optional.isPresent()) {
            ServiceDTO dto = serviceDTOAdapter.apply(optional.get());
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/services/page/{page}/rows/{rows}", produces = "application/json")
    public ResponseEntity<List<ServiceDTO>> getListService(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int page, @PathVariable int rows) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        List<ServiceDTO> listDTO = this.service.getListByUserId(userId, page - 1, rows).stream().map(serviceDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/services/row-count", produces = "application/json")
    public ResponseEntity<Long> getRowCount(@AuthenticationPrincipal UserDetails userDetails) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        long rowCount = this.service.getRowCountByUserId(userId);

        return ResponseEntity.ok().body(rowCount);
    }

    @GetMapping(value = "/services/all/page/{page}/rows/{rows}", produces = "application/json")
    public ResponseEntity<List<ServiceDTO>> getListAllService(@PathVariable int page, @PathVariable int rows) {
        List<ServiceDTO> listDTO = this.service.getList(page - 1, rows).stream().map(serviceDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/services/all/row-count", produces = "application/json")
    public ResponseEntity<Long> getAllRowCount() {
        long rowCount = this.service.getRowCount();

        return ResponseEntity.ok().body(rowCount);
    }

    @GetMapping(value = "/services/name-like/{name}", produces = "application/json")
    public ResponseEntity<List<ServiceDTO>> getListServiceByNameLike(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String name) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        List<ServiceDTO> listDTO = this.service.getListByNameLikeAndUserId(name, userId).stream().map(serviceDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @PostMapping(value = "/service")
    public ResponseEntity<Object> saveService(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody ServiceRequest request) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        User user = userService.getById(userId);
        Service entity = this.serviceRequestAdapter.toEntity(request, user);
        String message = this.service.getValidationsMessageWhenCantBeSaved(entity);

        if (message.isEmpty()) {
            entity = this.service.save(entity);
            ServiceDTO dto = this.serviceDTOAdapter.apply(entity);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    @DeleteMapping(value = "/service/{serviceId}")
    public ResponseEntity<Boolean> deleteServiceToUserId(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long serviceId) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);

        boolean deleted = this.service.deleteByServiceIdAndUserId(serviceId, userId);
        if (deleted) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }
}
