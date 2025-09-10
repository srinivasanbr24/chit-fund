package com.akb.chit_fund.controller;

import com.akb.chit_fund.dto.SchemaDTO;
import com.akb.chit_fund.model.Schema;
import com.akb.chit_fund.service.SchemaService;
import com.akb.chit_fund.utility.Utility;
import com.akb.chit_fund.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/schemas")
@AllArgsConstructor
@Validated
public class SchemaController {

    private final SchemaService schemaService;

    private static final Logger LOG = LoggerFactory.getLogger(SchemaController.class);

    @PostMapping("/create")
    @JsonView(Views.Basic.class)
    public SchemaDTO createSchema(@RequestBody SchemaDTO schema) {
        LOG.debug("Received create schema request for schema: {}", schema.getSchemaName());
        if(schema.getSchemaName() == null || schema.getSchemaName().isBlank()){
            throw new RuntimeException("Schema name cannot be empty");
        }
        if(schema.getDescription() == null || schema.getDescription().isBlank()){
            throw new RuntimeException("Schema description cannot be empty");
        }
        if(schema.getDurationInMonths() == null || schema.getDurationInMonths() <=0) {
            throw new RuntimeException(" Duration should be greater than 1 Month.");
        }
        if(schema.getMonthlyContribution() == null || schema.getMonthlyContribution() <=0){
            throw new RuntimeException(" Monthly Contribution should be greater than 0.");
        }
        LOG.info("Schema created Successfully with name: {}",schema.getSchemaName());
       return schemaService.createSchema(schema);

    }

    @PostMapping("/{schemaId}/addUser")
    @JsonView(Views.Detailed.class)
    public SchemaDTO addUserToSchema(@RequestBody @Valid AddUserRequest request, @PathVariable  @NotNull Long schemaId) {
        if(!Utility.isValidMobileNumber(request.getMobileNumber())){
            throw new RuntimeException("Invalid mobile number");
        }
        LOG.info("Received add user request to add to schemaId:{}, for user:{}",schemaId,request.mobileNumber);
        return schemaService.addUserToSchema(schemaId, request.getMobileNumber(), request.getUserName());
    }

    @GetMapping
    public List<Schema> getAllSchemas(){
        return schemaService.getAllSchemas();
    }

    @DeleteMapping("/{schemaId}/delete")
    public String deleteSchema(@PathVariable @NotNull Long schemaId){
        LOG.info("Received delete schema request for schemaId: {}",schemaId);
        return schemaService.removeSchema(schemaId);
    }

    @DeleteMapping("/{schemaId}/removeUser/{mobileNumber}")
    public String removeUserFromSchema(@PathVariable @NotNull Long schemaId, @PathVariable @NotBlank String mobileNumber){
        if(!Utility.isValidMobileNumber(mobileNumber)){
            throw new RuntimeException("Invalid mobile number");
        }
        LOG.info("Received remove user request to remove from schemaId:{}, for user:{}",schemaId,mobileNumber);
        return schemaService.removeUserFromSchema(schemaId, mobileNumber);
    }


    @Data
    static class AddUserRequest {
        @NotBlank(message = "Mobile number cannot be Empty")
        private String mobileNumber;
        @NotBlank(message = "User name cannot be Empty")
        private String userName;
    }
}
