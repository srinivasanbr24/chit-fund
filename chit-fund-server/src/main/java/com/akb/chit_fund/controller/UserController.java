package com.akb.chit_fund.controller;

import com.akb.chit_fund.service.UserService;
import com.akb.chit_fund.utility.Utility;
import com.akb.chit_fund.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
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
        LOG.info("Retrieve schemas for user :{}",mobileNumber);
        if(!Utility.isValidMobileNumber(mobileNumber)) {
            throw new RuntimeException("Invalid mobile number");
        }
        return userService.getUserSchemas(mobileNumber);
    }

    @PatchMapping("/updatePassword/{mobileNumber}")
    public String updatePassword(@PathVariable String mobileNumber, @RequestParam String oldPassword, @RequestParam String newPassword) {
        LOG.debug("Update password request received for user: {}",mobileNumber);
        if(!Utility.isValidMobileNumber(mobileNumber)) {
            throw new RuntimeException("Invalid mobile number");
        }
        if(StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            throw new RuntimeException("Passwords cannot be empty");
        }
        return userService.updatePassword(mobileNumber, oldPassword, newPassword);
    }
}
