package com.akb.chit_fund.controller;

import com.akb.chit_fund.service.UserService;
import com.akb.chit_fund.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/schemas/{mobileNumber}")
    @JsonView(Views.Detailed.class)
    public ResponseEntity<?> getUserSchemas(@PathVariable String mobileNumber) {
        return userService.getUserSchemas(mobileNumber);
    }

    @PostMapping("/addUsername/{mobileNumber}")
    public String addUserName(@PathVariable String mobileNumber, @RequestParam String userName) {
        return userService.addUserName(mobileNumber, userName);
    }

    @PatchMapping("/updatePassword/{mobileNumber}")
    public String updatePassword(@PathVariable String mobileNumber, @RequestParam String oldPassword, @RequestParam String newPassword) {
        return userService.updatePassword(mobileNumber, oldPassword, newPassword);
    }
}
