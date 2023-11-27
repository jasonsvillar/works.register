package com.jasonvillar.works.register.user;

import com.jasonvillar.works.register.authentication.SecurityUserDetailsService;
import com.jasonvillar.works.register.user.port.in.AddAdminRoleToUserRequest;
import com.jasonvillar.works.register.user.port.in.ChangePasswordRequest;
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
@Tag(name = "3 - User", description = "the user API for admin")
public class UserController {

    private final UserService service;

    private final UserRequestAdapter userRequestAdapter;

    private final UserDTOAdapter userDTOAdapter;

    private final SecurityUserDetailsService securityUserDetailsService;

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

    @Secured("Add user")
    @PostMapping(value = "/user")
    public ResponseEntity<Object> saveUser(@Valid @RequestBody UserRequest request) {
        User entity = this.userRequestAdapter.toEntity(request);
        String message = this.service.getValidationsMessageWhenCantBeSaved(entity);

        if (message.isEmpty()) {
            String passwordInBcrypt = this.service.plainPasswordToBcrypt(request.password());
            entity.setPassword(passwordInBcrypt);
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

    @SecurityRequirements
    @GetMapping(value = "/user/validate/name/{name}/email/{email}/code/{code}")
    public ResponseEntity<Object> validateUser(@PathVariable String name, @PathVariable String email, @PathVariable String code) {
        Optional<User> userOptional = this.service.getOptionalByNameAndEmailAndValidatedAndCode(name, email, false, code);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setCode(null);
            user.setValidated(true);

            user = service.save(user);
            UserDTO userDTO = userDTOAdapter.apply(user);
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/user/change-password")
    public ResponseEntity<String> resetPassword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ChangePasswordRequest changePasswordRequest) {
        if (!changePasswordRequest.newPassword().equals(changePasswordRequest.repeatNewPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The new password has differences");
        }

        long userId = this.securityUserDetailsService.getAuthenticatedUserId(userDetails);
        User user = this.service.getById(userId);

        if (this.service.passwordMatchWithActual(changePasswordRequest.oldPassword(), user.getPassword())) {
            user.setPassword(this.service.plainPasswordToBcrypt(changePasswordRequest.newPassword()));
            this.service.save(user);
            return ResponseEntity.ok("The password has been changed");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The actual password is incorrect");
        }
    }
}
