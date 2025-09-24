package com.akb.chit_fund.service;

import com.akb.chit_fund.dto.SchemaDTO;
import com.akb.chit_fund.model.Role;
import com.akb.chit_fund.model.User;
import com.akb.chit_fund.repository.UserRepository;
import com.akb.chit_fund.utility.Utility;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository  userRepository;

    private final BCryptPasswordEncoder encoder;

    private static final String TEMP_PASSWORD = "akbchitfund";
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public User registerUser(String mobileNumber, String password, String userName, Role role) {
        try {
            LOG.info("Registering {} with mobile: {}", role, mobileNumber);
            if (!Utility.isValidMobileNumber(mobileNumber)) {
                throw new IllegalArgumentException("Invalid mobile number");
            }
            if (userRepository.existsByMobileNumber(mobileNumber)) {
                throw new IllegalArgumentException("Mobile number already registered");
            }
            User u = new User();
            u.setMobileNumber(mobileNumber);
            u.setPassword(encoder.encode(Objects.requireNonNullElse(password, TEMP_PASSWORD)));
            u.setUserName(userName);
            u.setRole(role);
            LOG.info("{} registered successfully with mobile: {}", role, mobileNumber);
            return userRepository.save(u);
        } catch (Exception e) {
            LOG.error("Error while registering user: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Transactional(readOnly = true)
    public ResponseEntity<?> getUserSchemas(String mobileNumber) {
        try {
            LOG.info("Fetching schemas for user with Mobile: {}", mobileNumber);
            Optional<User> user = userRepository.findById(mobileNumber);
            if (user.isEmpty()) {
                String errorMessage = "No user found with mobile number: " + mobileNumber;
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
            Set<SchemaDTO> schemaDTOs = user.get().getSchemas().stream()
                    .map(schema -> new SchemaDTO(schema.getId(), schema.getName(), schema.getDescription(), schema.getDurationInMonths(), schema.getMonthlyContribution()))
                    .collect(Collectors.toSet());
            LOG.debug("Retrieved schemas for user :{}", schemaDTOs);
            return ResponseEntity.ok(schemaDTOs);
        } catch (Exception e) {
            LOG.error("Error while fetching user schemas: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Transactional
    public boolean updatePassword(String mobileNumber, String oldPassword, String newPassword) {
        try {
            LOG.info(" Updating password for user with mobile: {}", mobileNumber);
            User user = userRepository.findById(mobileNumber).orElseThrow(() -> new RuntimeException("No user found with mobile number: " + mobileNumber));
            if (encoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(encoder.encode(newPassword));
                userRepository.save(user);
                LOG.info("Password updated successfully for user with mobile: {}", mobileNumber);
                return true;
            } else {
                throw new RuntimeException("Please enter the old password correctly!!");
            }
        } catch (Exception e) {
            LOG.error("Error while updating password: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
