//package com.finance.dashboard.service;
//
//import com.finance.dashboard.dto.LoginRequest;
//import com.finance.dashboard.dto.RegisterRequest;
//import com.finance.dashboard.entity.Role;
//import com.finance.dashboard.entity.User;
//import com.finance.dashboard.repository.UserRepository;
//import com.finance.dashboard.security.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtUtil jwtUtil;
//
//    // 🔥 REGISTER USER
//    public String registerUser(RegisterRequest request) {
//
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new RuntimeException("Email already exists");
//        }
//
//        User user = User.builder()
//                .name(request.getName())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(Role.VIEWER) // default role
//                .active(true) // 🔥 important
//                .build();
//
//        userRepository.save(user);
//
//        return "User registered successfully";
//    }
//
//    // 🔥 LOGIN USER
//    public String loginUser(LoginRequest request) {
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 🔥 CHECK ACTIVE STATUS
//        if (!user.isActive()) {
//            throw new RuntimeException("User is inactive");
//        }
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid password");
//        }
//
//        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
//    }
//
//    // 🔥 UPDATE USER ROLE (ADMIN FEATURE)
//    public String updateUserRole(Long id, String role) {
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        try {
//            user.setRole(Role.valueOf(role.toUpperCase())); // 🔥 safe parsing
//        } catch (IllegalArgumentException e) {
//            throw new RuntimeException("Invalid role. Use ADMIN / ANALYST / VIEWER");
//        }
//
//        userRepository.save(user);
//
//        return "Role updated";
//    }
//
//    // 🔥 ACTIVATE / DEACTIVATE USER
//    public String updateUserStatus(Long id, boolean active) {
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        user.setActive(active);
//        userRepository.save(user);
//
//        return active ? "User activated" : "User deactivated";
//    }
//}




package com.finance.dashboard.service;

import com.finance.dashboard.dto.LoginRequest;
import com.finance.dashboard.dto.RegisterRequest;
import com.finance.dashboard.entity.Role;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 🔥 REGISTER USER - Now with Auto Admin for the first user
    public String registerUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Check if this is the first user in the entire system
        boolean isFirstUser = userRepository.count() == 0;

        // First user becomes ADMIN, others become VIEWER by default
        Role assignedRole = isFirstUser ? Role.ADMIN : Role.VIEWER;

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(assignedRole)
                .active(true)
                .build();

        userRepository.save(user);

        String roleMessage = isFirstUser ? "ADMIN (First user)" : "VIEWER";
        return "User registered successfully as " + roleMessage;
    }

    // 🔥 LOGIN USER
    public String loginUser(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 CHECK ACTIVE STATUS
        if (!user.isActive()) {
            throw new RuntimeException("User is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }

    // 🔥 UPDATE USER ROLE (ADMIN FEATURE ONLY)
    public String updateUserRole(Long id, String role) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            user.setRole(Role.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role. Use ADMIN / ANALYST / VIEWER");
        }

        userRepository.save(user);
        return "Role updated successfully to " + role.toUpperCase();
    }

    // 🔥 ACTIVATE / DEACTIVATE USER (ADMIN FEATURE ONLY)
    public String updateUserStatus(Long id, boolean active) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(active);
        userRepository.save(user);

        return active ? "User activated successfully" : "User deactivated successfully";
    }
}

