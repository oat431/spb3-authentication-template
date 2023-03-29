package sahachan.template.auth.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sahachan.template.auth.security.dto.UserDto;
import sahachan.template.auth.security.entity.Location;
import sahachan.template.auth.security.entity.User;
import sahachan.template.auth.security.service.UserService;
import sahachan.template.auth.util.BackendMapper;
import sahachan.template.auth.util.JwtTokenUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User API", description = "User Feature")
public class UserController {
    final UserService userService;
    final UserDetailsService userDetailsService;
    final JwtTokenUtil jwtTokenUtil;
    @GetMapping("/{id}")
    @Operation(summary = "User Get their data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User can Saw their data",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))})
    })
    ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(BackendMapper.INSTANCE.getUserDto(userService.getUser(id)));
    }

    @Operation(summary = "Update User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User can update their data after login",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))})
    })
    @PostMapping("/{id}/update")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody UpdateRequest updateRequest) {
        Location location = Location.builder()
                .province(updateRequest.getLocation().getProvince())
                .district(updateRequest.getLocation().getDistrict())
                .subDistrict(updateRequest.getLocation().getSubDistrict())
                .build();
        User updateUser = User.builder()
                .username("tempUsername")
                .password("tempPassword")
                .email("tempEmail")
                .firstname(updateRequest.getFirstname())
                .lastname(updateRequest.getLastname())
                .birthday(updateRequest.getBirthday())
                .lastPasswordResetDate(Date.from(LocalDate.of(2021, 01, 01).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .location(location)
                .build();
        return ResponseEntity.ok(BackendMapper.INSTANCE.getUserDto(userService.updateUser(id, updateUser)));
    }
    @Operation(summary = "User Can delete their account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User can their delete their data",
                    content = {
                        @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
                        )
            })
    })
    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
    @Operation(summary = "User Can Change their password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User can change their password"),
            @ApiResponse(responseCode = "400(1)", description = "current password is invalid"),
            @ApiResponse(responseCode = "400(2)", description = "new password can't be same as previous password")
    })
    @PostMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable("id") Long id,@RequestBody ChangePasswordRequest changePasswordRequest) {
        if(!checkCurrentPassword(id,changePasswordRequest.getCurrentPassword())){
            return ResponseEntity.badRequest().body("current password is invalid");
        }
        if(checkCurrentPassword(id,changePasswordRequest.getNewPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("new password can't be same as previous password");
        }
        userService.updatePassword(id, changePasswordRequest.getNewPassword());
        return ResponseEntity.ok("Your Password are Changed");
    }

    @Operation(summary = "User Can Change their email")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User can change their email",
                    content = {
                        @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChangeEmailResponse.class)
                        )
                    }
            ),
            @ApiResponse(
                    responseCode = "400(1)",
                    description = "invalid password",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ChangeEmailResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400(2)",
                    description = "new email can't be same as previous email",
                    content = {
                        @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChangeEmailResponse.class)
                        )
                    }
            ),
            @ApiResponse(
                    responseCode = "400(3)",
                    description = "this email already exist",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ChangeEmailResponse.class)
                            )
                    }
            )
    })
    @PostMapping("/{id}/change-email")
    public ResponseEntity<?> changeEmail(@PathVariable("id") Long id, @RequestBody ChangeEmailRequest changeEmailRequest) {
        Map result = new HashMap<>();
        if(!checkCurrentPassword(id,changeEmailRequest.getConfirmPassword())) {
            result.put("response","invalid password");
            result.put("token","");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        if(checkCurrentEmail(id,changeEmailRequest.getNewEmail())) {
            result.put("response","new email can't be same as previous email");
            result.put("token","");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        if(checkDuplicateEmail(changeEmailRequest.getNewEmail())) {
            result.put("response","this email already exist");
            result.put("token","");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        userService.updateEmail(id, changeEmailRequest.getNewEmail());
        result.put("response","Your Email are changed");
        result.put("token",getNewToken(id,changeEmailRequest.getNewEmail()));
        return ResponseEntity.ok(result);
    }

    private Boolean checkCurrentPassword(Long id,String newPassword) {
        User user = userService.getUser(id);
        return new BCryptPasswordEncoder().matches(newPassword,user.getPassword());
    }

    private Boolean checkCurrentEmail(Long id, String newEmail) {
        User user = userService.getUser(id);
        return newEmail.equals(user.getEmail());
    }

    private Boolean checkDuplicateEmail(String email) {
        return userService.findByUsername(email) != null;
    }

    private String getNewToken(Long id, String newEmail) {
        User user = userService.getUser(id);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(newEmail);
        return jwtTokenUtil.generateToken(userDetails, user.getAuthorities(),user.getId(),86400L);
    }
}
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UpdateRequest", description = "Update API required parameter")
class UpdateRequest {

    @Schema(description = "Firstname", example = "Sahachan", required = true)
    String firstname;

    @Schema(description = "Lastname", example = "Tippimwong", required = true)
    String lastname;

    @Schema(description = "birthday", example = "2000-11-09", required = true)
    Date birthday;

    @Schema(description = "Location", required = true)
    UpdateLocationRequest location;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UpdateLocationRequest", description = "Location Object required parameter")
class UpdateLocationRequest {
    @Schema(description = "SubDistrict", example = "สุเทพ", required = true)
    String subDistrict;

    @Schema(description = "District", example = "เมืองเชียงใหม่", required = true)
    String district;

    @Schema(description = "Province", example = "เชียงใหม่", required = true)
    String province;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ChangePasswordRequest", description = "Change Password API required parameter")
class ChangePasswordRequest {
    @Schema(description = "Current Password", example = "12345678", required = true)
    String currentPassword;
    @Schema(description = "New Password", example = "87654321", required = true)
    String newPassword;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ChangeEmailRequest", description = "Change Email API required parameter")
class ChangeEmailRequest {
    @Schema(description = "New Email", example = "name@email.domain", required = true)
    String newEmail;

    @Schema(description = "Confirm password for changing email", example = "1234567", required = true)
    String confirmPassword;

}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ChangeEmailResponse", description = "Change Email API response")
class ChangeEmailResponse {
    @Schema(description = "Response", example = "name@email.domain", required = true)
    String response;
    @Schema(description = "new Token", example = "eyJhbGciO...", required = true)
    String token;
}