package com.finance.dashboard.controller;

import com.finance.dashboard.dto.ApiResponse;
import com.finance.dashboard.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "03. User Management", description = "Admin user management APIs")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ====================== ADMIN ONLY ======================
    @Operation(summary = "3.1 Update User Role")
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> updateRole(@PathVariable Long id,
                                          @RequestParam String role) {
        return new ApiResponse<>(
                true,
                userService.updateUserRole(id, role),
                null
        );
    }

    @Operation(summary = "3.2 Activate / Deactivate User")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> updateStatus(@PathVariable Long id,
                                            @RequestParam boolean active) {
        return new ApiResponse<>(
                true,
                userService.updateUserStatus(id, active),
                null
        );
    }
}