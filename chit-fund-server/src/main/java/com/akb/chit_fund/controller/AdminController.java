package com.akb.chit_fund.controller;

import com.akb.chit_fund.model.Role;
import com.akb.chit_fund.model.User;
import com.akb.chit_fund.repository.UserRepository;
import com.akb.chit_fund.service.UserService;
import com.akb.chit_fund.utility.JWTUtility;
import com.akb.chit_fund.utility.Utility;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Hidden
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AdminController {

    private final UserRepository userRepo;
    private final JWTUtility jwtUtil;
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;

private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    @PostMapping("/registerAdmin")
    public ResponseEntity<String> registerAdmin(@RequestBody @Valid RegisterRequest request) {
        LOG.debug("Register admin request for mobile: {}", request.getMobile());
        String mobileNumber = request.getMobile();

        if(!Utility.isValidMobileNumber(mobileNumber)) {
            throw new RuntimeException("Invalid mobile number");
        }
        if(userRepo.findById(mobileNumber).isPresent()) {
            throw new RuntimeException("Admin with this mobile number already exists");
        }
        User user = userService.registerUser(mobileNumber, request.getPassword(), request.getUserName(), Role.ADMIN);
        LOG.info("Admin registered successfully with mobile: {}", user.getMobileNumber());
        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest request) {
        LOG.info("Received login request for mobile: {}", request.getMobile());
        String mobile = request.getMobile();
        String password = request.getPassword();
        if(!Utility.isValidMobileNumber(mobile)) {
            throw new RuntimeException("Invalid mobile number");
        }
        User u = userRepo.findById(mobile).orElseThrow(() -> new RuntimeException("User Not found"));
        if (!encoder.matches(password, u.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        LOG.info("User logged in successfully with mobile: {}", u.getMobileNumber());
        return ResponseEntity.ok(jwtUtil.generateToken(u.getMobileNumber(), u.getRole().name()));
    }

    @Data
    static class RegisterRequest {
        @NotNull(message = "Mobile number cannot be Empty")
        private String mobile;

        @NotNull(message = "Password cannot be Empty")
        @Size(min=8, message = "Password should have at least 8 characters")
        private String password;

        @NotNull(message = "User name cannot be Empty")
        @Size(min = 5, message = "User name should have at least 3 characters")
        private String userName;
    }

    @Data
    static class LoginRequest {
        @NotBlank(message = "Mobile number cannot be Empty")
        private String mobile;
        @NotBlank (message = "Password cannot be Empty")
        private String password;
    }
}
