package panomete.jwtauth.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import panomete.jwtauth.security.payload.request.RegisterRequest;
import panomete.jwtauth.security.service.AuthService;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication API", description = "the authentication API")
public class AuthController {
    final AuthService authService;
    /*
     * todo: implement auth controller by
     * - /auth/details (GET) (required token)
     * - /auth/refresh (GET)
     * - /auth/credentials (GET)
     * - /auth/register (POST) (body: Users.java)
     * - /auth/login (POST) (body: {username, password})
     * - /auth/forgot-password (POST)
     * - /auth/reset-password (POST)
     */
    @PostMapping("/signup")
    @Operation(summary = "Create a new user", description = "Create a new user")
    public ResponseEntity<?> createUserAccount(@RequestBody RegisterRequest user){
        authService.createUser(user);
        return ResponseEntity.ok("User created successfully!");
    }
}
