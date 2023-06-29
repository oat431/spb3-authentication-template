package panomete.jwtauth.admins.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import panomete.jwtauth.admins.service.AdminService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Admin API", description = "the admin API")
public class AdminController {
    final AdminService adminService;
    /*
     * todo: implement
     * - /users (GET) - get all users
     * - /users/{id} (GET) - get user by id
     * - /users/{id} (PUT) - update user by id
     *   - ?permissions=[String] - update user permissions
     *   - ?authorities=[String] - update user authorities
     * - /users/{id} (DELETE) - delete user by id
     */

    @PatchMapping("/users/{id}/")
    @Operation(summary = "Enable or Disable user account", description = "Enable or Disable user account on the platform")
    public ResponseEntity<?> enableAccount(@PathVariable("id") String uuid) {
        return ResponseEntity.ok(adminService.enableAccount(uuid));
    }

    @DeleteMapping("/users/{id}/")
    @Operation(summary = "Delete user account", description = "Delete user account on the platform")
    public ResponseEntity<?> deleteAccount(@PathVariable("id") String uuid) {
        return ResponseEntity.ok(adminService.deleteAccount(uuid));
    }
}
