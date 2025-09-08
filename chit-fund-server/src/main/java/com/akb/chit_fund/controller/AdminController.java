package com.akb.chit_fund.controller;

import com.akb.chit_fund.model.Role;
import com.akb.chit_fund.model.User;
import com.akb.chit_fund.repository.UserRepository;
import com.akb.chit_fund.service.UserService;
import com.akb.chit_fund.utility.JWTUtility;
import com.akb.chit_fund.utility.Utility;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AdminController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JWTUtility jwtUtil;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private UserService userService;


private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    @PostMapping("/registerAdmin")
    public String registerAdmin(@RequestParam String mobileNumber, @RequestParam String password, @RequestParam String userName) {
        User user = userService.registerUser(mobileNumber, password, userName, Role.ADMIN);
        return "Admin registered successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        LOG.debug(" Received login request for mobile: {}", request.getMobile());
        String mobile = request.getMobile();
        String password = request.getPassword();
        if(!Utility.isValidMobileNumber(mobile)) {
            throw new RuntimeException("Invalid mobile number");
        }
        User u = userRepo.findById(mobile).orElseThrow(() -> new RuntimeException("User Not found"));
        if (!encoder.matches(password, u.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return ResponseEntity.ok(jwtUtil.generateToken(u.getMobileNumber(), u.getRole().name()));
    }

    
    @Data
    static class LoginRequest {
        private String mobile;
        private String password;
    }
}
