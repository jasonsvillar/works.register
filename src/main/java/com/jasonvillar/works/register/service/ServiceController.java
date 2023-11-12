package com.jasonvillar.works.register.service;

import com.jasonvillar.works.register.authentication.SecurityUserDetailsService;
import com.jasonvillar.works.register.service.port.in.ServiceRequestAdapter;
import com.jasonvillar.works.register.service.port.out.ServiceDTO;
import com.jasonvillar.works.register.service.port.out.ServiceDTOAdapter;
import com.jasonvillar.works.register.service.port.in.ServiceRequest;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user_service.UserService;
import com.jasonvillar.works.register.user_service.UserServiceService;
import com.jasonvillar.works.register.user_service.port.in.UserServiceRequest;
import com.jasonvillar.works.register.user_service.port.out.UserServiceDTO;
import com.jasonvillar.works.register.user_service.port.out.UserServiceDTOAdapter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "4 - Service", description = "the service API tag annotation")
public class ServiceController {
    private final ServiceService service;

    private final ServiceDTOAdapter serviceDTOAdapter;

    private final ServiceRequestAdapter serviceRequestAdapter;

    private final SecurityUserDetailsService securityUserDetailsService;

    private final UserServiceService userServiceService;

    private final UserServiceDTOAdapter userServiceDTOAdapter;

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

    @GetMapping(value = "/services/unused/page/{page}/rows/{rows}", produces = "application/json")
    public ResponseEntity<List<ServiceDTO>> getListUnusedServiceFromUserId(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int page, @PathVariable int rows) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        List<ServiceDTO> listDTO = this.service.getUnusedListFromUserId(userId, page - 1, rows).stream().map(serviceDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @GetMapping(value = "/services/unused/row-count", produces = "application/json")
    public ResponseEntity<Long> getUnusedRowCountFromUserId(@AuthenticationPrincipal UserDetails userDetails) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        long rowCount = this.service.getUnusedRowCountFromUserId(userId);

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
        Service entity = this.serviceRequestAdapter.toEntity(request);
        String message = this.service.getValidationsMessageWhenCantBeSaved(entity);

        if (message.isEmpty()) {
            entity = this.service.saveWithUser(entity, userId);
            ServiceDTO dto = this.serviceDTOAdapter.apply(entity);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    @PostMapping(value = "/service/user")
    public ResponseEntity<Object> saveUserService(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UserServiceRequest request) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);

        long serviceId = request.serviceId();

        boolean existService = this.service.isExistId(serviceId);

        if (!existService) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not founded");
        }

        UserService entity = UserService.builder()
                .serviceId(serviceId)
                .userId(userId)
                .build();

        boolean existUserService = this.userServiceService.isExistUserIdAndServiceId(userId, serviceId);

        if (existUserService) {
            return ResponseEntity.status(HttpStatus.FOUND).body("Found existent user service");
        } else {
            entity = this.userServiceService.save(entity);
            entity.setUser(this.userService.getById(userId));
            entity.setService(this.service.getById(serviceId));

            UserServiceDTO dto = this.userServiceDTOAdapter.apply(entity);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        }
    }

    @DeleteMapping(value = "/service/{serviceId}")
    public ResponseEntity<Boolean> deleteServiceToUserId(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long serviceId) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);

        boolean deleted = this.userServiceService.deleteByServiceIdAndUserId(serviceId, userId);
        if (deleted) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/services/delete/batch")
    public ResponseEntity<List<UserServiceDTO>> deleteServicesBatchToUserId(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody List<Long> serviceIdLongList) {
        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        User user = this.userService.getById(userId);

        long[] serviceIdLongArray = serviceIdLongList.stream().mapToLong(l -> l).toArray();

        List<UserService> userServiceListDeleted = this.userServiceService.deleteByServicesIdAndUserId(serviceIdLongArray, userId);

        userServiceListDeleted.forEach(
            userService -> {
                userService.setUser(user);
                Service service = this.service.getById(userService.getServiceId());
                userService.setService(service);
            }
        );

        List<UserServiceDTO> userServiceListDTO = userServiceListDeleted.stream().map(this.userServiceDTOAdapter).toList();

        return new ResponseEntity<>(userServiceListDTO, HttpStatus.OK);
    }
}
