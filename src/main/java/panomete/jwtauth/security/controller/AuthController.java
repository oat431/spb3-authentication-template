package panomete.jwtauth.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import panomete.jwtauth.security.entity.Users;
import panomete.jwtauth.security.payload.request.LoginRequest;
import panomete.jwtauth.security.payload.request.RegisterRequest;
import panomete.jwtauth.security.payload.response.JwtResponse;
import panomete.jwtauth.security.service.AuthService;
import panomete.jwtauth.utility.DtoMapper;
import panomete.jwtauth.utility.JwtTokenUtil;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication API", description = "the authentication API")
public class AuthController {
    final AuthService authService;
    final AuthenticationManager authenticationManager;
    final JwtTokenUtil jwtTokenUtil;
    final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    /*
     * todo: implement auth controller by
     * - /auth/refresh (GET)
     * - /auth/credentials (GET)
     * - /auth/forgot-password (POST)
     * - /auth/reset-password (POST)
     */
    @PostMapping("/")
    @Operation(summary = "Login", description = "Login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login){
        if(login.getUsername() == null){
            return ResponseEntity.ok(createToken(loginWithEmail(login.getEmail(), login.getPassword())));
        }
        if(login.getEmail() == null){
            return ResponseEntity.ok(createToken(loginWithUsername(login.getUsername(), login.getPassword())));
        }
        return ResponseEntity.badRequest().build();
    }

    private JwtResponse createToken(Users user) {
        return JwtResponse.builder()
                .token(jwtTokenUtil.generateJWT(user, 604800L))
                .build();
    }

    private Users loginWithUsername(String username, String password) {
        Users user = authService.getUserByUsername(username);
        return loginProcess(username, password, user);
    }

    private Users loginWithEmail(String email, String password) {
        Users user = authService.getUserByEmail(email);
        return loginProcess(email, password, user);
    }

    private Users loginProcess(String username, String password, Users user) {
        if(!checkAuth(user,password)){return null;}
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("user {} successfully logged in", username);
        } catch (Exception e){
            log.error("login process error: {}", e.getMessage());
        }
        return user;
    }

    private Boolean checkAuth(Users user, String password){
        if(user == null){
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }

    @PostMapping("/signup")
    @Operation(summary = "Create a new user", description = "Create a new user")
    public ResponseEntity<?> createUserAccount(@RequestBody RegisterRequest user){
        Users newAccount = authService.createUser(user);
        return ResponseEntity.ok(
                DtoMapper.INSTANCE.toAuthDto(newAccount)
        );
    }

    @GetMapping("/details")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get user details", description = "Get user details")
    public ResponseEntity<?> getUserDetails(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        Users user = authService.getUserByUsername(username);
        return ResponseEntity.ok(
                DtoMapper.INSTANCE.toAuthDto(user)
        );
    }
}
