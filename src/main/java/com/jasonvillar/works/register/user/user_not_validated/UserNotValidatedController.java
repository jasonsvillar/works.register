package com.jasonvillar.works.register.user.user_not_validated;

import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user.UserService;
import com.jasonvillar.works.register.user.port.out.UserDTO;
import com.jasonvillar.works.register.user.port.out.UserDTOAdapter;
import com.jasonvillar.works.register.user.user_not_validated.port.in.UserNotValidatedRequest;
import com.jasonvillar.works.register.user.user_not_validated.port.in.UserNotValidatedRequestAdapter;
import com.jasonvillar.works.register.user.user_not_validated.port.out.UserNotValidatedDTO;
import com.jasonvillar.works.register.user.user_not_validated.port.out.UserNotValidatedDTOAdapter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "1 - Register User", description = "for user registration and validation")
public class UserNotValidatedController {
    private final UserNotValidatedService userNotValidatedService;

    private final UserNotValidatedRequestAdapter userNotValidatedRequestAdapter;

    private final UserNotValidatedDTOAdapter userNotValidatedDTOAdapter;

    private final UserService userService;

    private final UserDTOAdapter userDTOAdapter;

    @SecurityRequirements
    @PostMapping(value = "/pre-user")
    public ResponseEntity<Object> savePreUser(@Valid @RequestBody UserNotValidatedRequest request) {
        UserNotValidated userNotValidated = this.userNotValidatedRequestAdapter.toEntity(request);
        Optional<User> userOptional = this.userService.getOptionalByNameAndEmailAndValidated(request.name(), request.email(), true);

        if (userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("This is a valid user");
        }

        userNotValidated.setCode(this.userNotValidatedService.makeRandomValidationCode());
        userNotValidated.setPassword(this.userService.plainPasswordToBcrypt(request.password()));

        String frontEndValidationUrl = request.frontendUrlForValidating();

        if (userNotValidatedService.sendValidationCode(request.name(), request.email(), userNotValidated.getCode(), frontEndValidationUrl, false)) {
            userNotValidated = this.userNotValidatedService.save(userNotValidated);
            UserNotValidatedDTO userNotValidatedDTO = this.userNotValidatedDTOAdapter.apply(userNotValidated);
            return new ResponseEntity<>(userNotValidatedDTO, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Error sending the email");
        }
    }

    @SecurityRequirements
    @GetMapping(value = "/pre-user/validate/name/{name}/email/{email}/code/{code}")
    public ResponseEntity<Object> validatePreUser(@PathVariable String name, @PathVariable String email, @PathVariable String code) {
        UserNotValidatedId id = UserNotValidatedId.builder()
                .name(name)
                .email(email)
                .build();

        Optional<UserNotValidated> userNotValidatedOptional = userNotValidatedService.findOptionalByIdAndCode(id, code);

        if (userNotValidatedOptional.isPresent()) {
            boolean deleted = userNotValidatedService.deleteById(id);

            if (deleted) {
                UserNotValidated userNotValidated = userNotValidatedOptional.get();

                User user = User.builder()
                        .name(userNotValidated.getUserNotValidatedId().getName())
                        .email(userNotValidated.getUserNotValidatedId().getEmail())
                        .password(userNotValidated.getPassword())
                        .validated(true)
                        .build();

                user = userService.save(user);
                UserDTO userDTO = userDTOAdapter.apply(user);
                return ResponseEntity.ok(userDTO);
            } else {
                return ResponseEntity.internalServerError().body("Can't delete the not validated user");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
