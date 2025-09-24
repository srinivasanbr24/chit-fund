package com.akb.chit_fund.controller;

import com.akb.chit_fund.service.UserService;
import com.akb.chit_fund.utility.Utility;
import com.akb.chit_fund.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/schemas/{mobileNumber}")
    @JsonView(Views.Detailed.class)
    public ResponseEntity<?> getUserSchemas(@PathVariable String mobileNumber) {
        try {
            LOG.info("Retrieve schemas for user :{}", mobileNumber);
            if (!Utility.isValidMobileNumber(mobileNumber)) {
                throw new RuntimeException("Invalid mobile number");
            }
            return userService.getUserSchemas(mobileNumber);
        } catch (Exception e) {
            LOG.error("Error retrieving user schemas: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @PatchMapping("/updatePassword/{mobileNumber}")
    public boolean updatePassword(@PathVariable String mobileNumber, @RequestBody @Valid UpdateRequest request) {
        try {
            LOG.debug("Update password request received for user: {}", mobileNumber);
            if (!Utility.isValidMobileNumber(mobileNumber)) {
                throw new RuntimeException("Invalid mobile number");
            }
            if (StringUtils.isBlank(request.getOldPassword()) || StringUtils.isBlank(request.getNewPassword())) {
                throw new RuntimeException("Passwords cannot be empty");
            }
            return userService.updatePassword(mobileNumber, request.getOldPassword(), request.getNewPassword());
        } catch (Exception e) {
            LOG.error("Error updating password for user {}: {}", mobileNumber, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Data
    static class UpdateRequest{
        @NotNull(message = "Password cannot be Empty")
        @Size(min=8, message = "Old Password should have at least 8 characters")
        private String oldPassword;
        @NotNull(message = "Password cannot be Empty")
        @Size(min=8, message = "New Password should have at least 8 characters")
        private String newPassword;
    }
}
