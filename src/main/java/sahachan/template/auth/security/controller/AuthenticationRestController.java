package sahachan.template.auth.security.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sahachan.template.auth.security.dto.UserDto;
import sahachan.template.auth.security.entity.JwtUser;
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
@RequestMapping("${jwt.route.authentication.path}")
@Tag(name = "Authentication API", description = "Authentication Feature")
public class AuthenticationRestController {

    @Value("${jwt.header}")
    String tokenHeader;
    final AuthenticationManager authenticationManager;
    final JwtTokenUtil jwtTokenUtil;
    final UserDetailsService userDetailsService;
    final UserService userService;

    @Operation(summary = "Login for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login to System by username and password",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccessToken.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Invalid username or password"),
            @ApiResponse(responseCode = "4001", description = "Email not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws AuthenticationException {
        final Authentication authentication;
        User checkEmail = userService.findByUsername(authenticationRequest.getEmail());
        if (checkEmail == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found");
        }
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        User user = userService.findByUsername(authenticationRequest.getEmail());
//        if (user.getAdmin() != null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
//        }
        final String token = jwtTokenUtil.generateToken(userDetails, user.getAuthorities(), user.getId(), 86400L);
        Map result = new HashMap();
        result.put("token", token);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Login for admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login to System by admin username and admin password",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccessToken.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Invalid admin username or admin password"),
            @ApiResponse(responseCode = "4001", description = "Email not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login/admin")
    public ResponseEntity<?> createAuthenticationTokenForAdmin(@RequestBody LoginRequest authenticationRequest) throws AuthenticationException {
        final Authentication authentication;
        User checkEmail = userService.findByUsername(authenticationRequest.getEmail());
        if (checkEmail == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found");
        }
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid admin username or admin password");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        User user = userService.findByUsername(authenticationRequest.getEmail());
//        if (user.getMdUser() != null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid admin username or admin password");
//        }
        final String token = jwtTokenUtil.generateToken(userDetails, user.getAuthorities(), user.getId(), 10800L);
        Map result = new HashMap();
        result.put("token", token);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Refresh token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Refresh Token by given old token",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccessToken.class)
                    )},
                    headers = {@Header(
                            name = "Authorization",
                            description = "Bearer token",
                            required = true,
                            schema = @Schema(type = "string"))
                    }
            )
    })
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("${jwt.route.authentication.refresh}")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        token = getToken(token);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        long expiration;
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            expiration = 10800L;
        } else {
            expiration = 86400L;
        }
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token, expiration);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "get Credential")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Get user data from token",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
                    )},
                    headers = {@Header(
                            name = "Authorization",
                            description = "Bearer token",
                            required = true,
                            schema = @Schema(type = "string")
                    )}
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("credential")
    public ResponseEntity<?> getCredential(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        token = getToken(token);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(BackendMapper.INSTANCE.getUserDto(user));
    }

    private String getToken(String authToken) {
        if (authToken != null && authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
        }
        return authToken;
    }

    @Operation(summary = "Register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register new user to the system",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))})
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest register) {
        Location location = Location.builder()
                .province(register.getLocation().province)
                .district(register.getLocation().district)
                .subDistrict(register.getLocation().subDistrict)
                .build();
        User registered = User.builder()
                .username(register.getEmail())
                .password(register.getPassword())
                .email(register.getEmail())
                .birthday(register.getBirthday())
                .firstname(register.getFirstname())
                .lastname(register.getLastname())
                .location(location)
                .lastPasswordResetDate(Date.from(LocalDate.of(2021, 01, 01).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();
        if (userService.checkEmail(register.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exist");
        }
        User user = userService.addUser(registered);
        return ResponseEntity.ok(BackendMapper.INSTANCE.getUserDto(user));
    }

    @Operation(summary = "Reset Password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset password by email")
    })
    @SneakyThrows
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        User user = userService.findByUsername(resetPasswordRequest.getEmail());
//        emailService.sendResetPasswordEmail(resetPasswordRequest.getEmail(), user);
        return ResponseEntity.ok("Email had sent");
    }

    @Operation(summary = "Confirm Action")
    @PostMapping("/confirm-action")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Confirm action by password",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConfirmActionResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Password incorrect")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> confirmPassword(
            @RequestBody ConfirmActionRequest confirmActionRequest,
            HttpServletRequest request
    ) {
        String token = request.getHeader(tokenHeader);
        token = getToken(token);
        User user = userService.findByUsername(jwtTokenUtil.getUsernameFromToken(token));
        if (!checkCurrentPassword(user.getId(), confirmActionRequest.getConfirmPassword())) {
            return new ResponseEntity<>(
                    "Password incorrect",
                    HttpStatus.BAD_REQUEST
            );
        }
        Map result = new HashMap();
        JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(user.getUsername());
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            result.put("isRoleAdmin", true);
            result.put("isRoleUser", false);
        } else {
            result.put("isRoleAdmin", false);
            result.put("isRoleUser", true);
        }
        return new ResponseEntity<>(
                result,
                HttpStatus.OK
        );
    }

    private Boolean checkCurrentPassword(Long id, String newPassword) {
        User user = userService.getUser(id);
        return new BCryptPasswordEncoder().matches(newPassword, user.getPassword());
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "LoginRequest", description = "Login api required parameter")
class LoginRequest {
    @Schema(description = "Email", example = "user0@user.com", required = true)
    String email;

    @Schema(description = "Password", example = "user0", required = true)
    String password;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "RegisterRequest", description = "Register api required parameter")
class RegisterRequest {

    @Schema(description = "Email", example = "oat431@gmail.com", required = true)
    String email;

    @Schema(description = "Password", example = "1+Plus+1", required = true)
    String password;

    @Schema(description = "Firstname", example = "Sahachan", required = true)
    String firstname;

    @Schema(description = "Lastname", example = "Tippimwong", required = true)
    String lastname;

    @Schema(description = "birthday", example = "2000-11-09", required = true)
    Date birthday;

    @Schema(description = "Location", required = true)
    LocationRequest location;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UpdateLocationRequest", description = "Location Object required parameter")
class LocationRequest {
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
@Schema(name = "ResetPasswordRequest", description = "Reset Password API required parameter")
class ResetPasswordRequest {
    @Schema(description = "Email for reset password", example = "sahachan_t@cmu.ac.th", required = true)
    String email;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "AccessToken", description = "Token after request Login and Refresh Token")
class AccessToken {
    @Schema(description = "user token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdW...", required = false)
    String token;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ConfirmActionRequest", description = "Confirm Password API required parameter")
class ConfirmActionRequest {
    @Schema(description = "Email for reset password", example = "141516", required = true)
    String confirmPassword;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class ConfirmActionResponse {
    @Schema(description = "response for admin action", example = "true", required = true)
    Boolean isRoleAdmin;
    @Schema(description = "response for user action", example = "false", required = true)
    Boolean isRoleUser;
}