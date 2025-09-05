package com.akb.chit_fund.service;

import com.akb.chit_fund.dto.SchemaDTO;
import com.akb.chit_fund.model.Role;
import com.akb.chit_fund.model.User;
import com.akb.chit_fund.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    private UserRepository  userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private  static final String TEMP_PASSWORD = "akbchitfund";

    @Transactional
    public User registerUser(String mobileNumber, String password, String userName, Role role) {
        validateMobileNumber(mobileNumber);

        if(userRepository.existsByMobileNumber(mobileNumber)){
            throw new IllegalArgumentException("Mobile number already registered");
        }
        User u = new User();
        u.setMobileNumber(mobileNumber);
        u.setPassword(encoder.encode(Objects.requireNonNullElse(password, TEMP_PASSWORD)));
        u.setUserName(userName);
        u.setRole(role);
       return userRepository.save(u);
    }


    @Transactional(readOnly = true)
    public ResponseEntity<?> getUserSchemas(String mobileNumber) {
        Optional<User> user = userRepository.findById(mobileNumber);
        if(user.isEmpty())
        {
                String errorMessage = "No user found with mobile number: " + mobileNumber;
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }

        Set<SchemaDTO> schemaDTOs = user.get().getSchemas().stream()
                .map(schema -> new SchemaDTO(schema.getId(), schema.getName(),schema.getDescription(),schema.getDurationInMonths(),schema.getMonthlyContribution()))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(schemaDTOs);
    }

    @Transactional
    public String addUserName(String mobileNumber, String name){
        validateMobileNumber(mobileNumber);
        Optional<User> user = userRepository.findById(mobileNumber);
        if(user.isEmpty()) throw  new RuntimeException("No user found with mobile number: " + mobileNumber);
        user.get().setUserName(name);
        userRepository.save(user.get());
        return "Username Updated Successfully!!";
    }

    @Transactional
    public String updatePassword(String mobileNumber, String oldPassword, String newPassword) {
        validateMobileNumber(mobileNumber);
        Optional<User> user = userRepository.findById(mobileNumber);
        if(user.isEmpty()) throw  new RuntimeException("No user found with mobile number: " + mobileNumber);
        if(encoder.matches(oldPassword,TEMP_PASSWORD)) {
            user.get().setPassword(encoder.encode(newPassword));
            userRepository.save(user.get());
        } else throw new RuntimeException("Please enter the old password correctly!!");
        return "Password Updated Successfully !!!";
    }

    //private void

    private void validateMobileNumber(String mobileNumber) {
        if (mobileNumber == null || !mobileNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Invalid mobile number");
        }
    }
}
