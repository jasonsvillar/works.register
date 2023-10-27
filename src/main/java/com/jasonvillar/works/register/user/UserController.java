package com.jasonvillar.works.register.user;

import com.jasonvillar.works.register.user.port.in.AddAdminRoleToUserRequest;
import com.jasonvillar.works.register.user.port.in.UserRequestAdapter;
import com.jasonvillar.works.register.user.port.out.UserDTOAdapter;
import com.jasonvillar.works.register.user.port.in.UserRequest;
import com.jasonvillar.works.register.user.port.out.UserDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "2 - User", description = "the user API tag annotation")
public class UserController {

    private final UserService service;

    private final UserRequestAdapter userRequestAdapter;

    private final UserDTOAdapter userDTOAdapter;

    @Secured("View users")
    @GetMapping(value = "/user/{id}", produces = "application/json")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        Optional<User> optional = this.service.getOptionalById(id);
        if (optional.isPresent()) {
            UserDTO dto = userDTOAdapter.apply(optional.get());
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Secured("View users")
    @GetMapping(value = "/users",produces = "application/json")
    public ResponseEntity<List<UserDTO>> getListUser() {
        List<UserDTO> listDTO = this.service.getList().stream().map(userDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @Secured("View users")
    @GetMapping(value = "/users/name-like/{name}", produces = "application/json")
    public ResponseEntity<List<UserDTO>> getListUserByNameLike(@PathVariable String name) {
        List<UserDTO> listDTO = this.service.getListByNameLike(name).stream().map(userDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @Secured("View users")
    @GetMapping(value = "/users/email-like/{email}", produces = "application/json")
    public ResponseEntity<List<UserDTO>> getListUserByEmailLike(@PathVariable String email) {
        List<UserDTO> listDTO = this.service.getListByEmailLike(email).stream().map(userDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @Secured("View users")
    @GetMapping(value = "/users/name-like/{name}/email-like/{email}", produces = "application/json")
    public ResponseEntity<List<UserDTO>> getListUserByNameLikeAndEmailLike(@PathVariable String name, @PathVariable String email) {
        List<UserDTO> listDTO = this.service.getListByNameLikeAndEmailLike(name, email).stream().map(userDTOAdapter).toList();

        if (listDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(listDTO);
        }
    }

    @SecurityRequirements
    @PostMapping(value = "/user")
    public ResponseEntity<Object> saveUser(@Valid @RequestBody UserRequest request) {
        User entity = this.userRequestAdapter.toEntity(request);
        String message = this.service.getValidationsMessageWhenCantBeSaved(entity);

        if (message.isEmpty()) {
            entity = this.service.save(entity);
            UserDTO dto = this.userDTOAdapter.apply(entity);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    @Secured("Add admin role to user")
    @PostMapping(value = "/user/add-role/admin")
    public ResponseEntity<String> addAdminRoleToUser(@Valid @RequestBody AddAdminRoleToUserRequest request) {
        if(this.service.addAdminRoleToUserById(request.id())) {
            return new ResponseEntity<>("Added admin role to user with id " + request.id(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("An error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
