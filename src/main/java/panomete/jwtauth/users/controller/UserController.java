package panomete.jwtauth.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import panomete.jwtauth.users.service.UserService;
import panomete.jwtauth.utility.DtoMapper;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "the user API")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "See other user profile", description = "See other user profile on the platform")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        return ResponseEntity.ok(DtoMapper.INSTANCE.toAuthDto(userService.getUserByUUID(id)));
    }
}
