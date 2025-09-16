package com.akb.chit_fund.controller;

import com.akb.chit_fund.dto.SchemaDTO;
import com.akb.chit_fund.model.Schema;
import com.akb.chit_fund.service.SchemaService;
import com.akb.chit_fund.utility.Utility;
import com.akb.chit_fund.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    public SchemaDTO createSchema( @Validated @RequestBody SchemaDTO schema) {
        try{
            LOG.debug("Received create schema request for schema: {}", schema.getSchemaName());
            return schemaService.createSchema(schema);
        } catch (Exception e){
            LOG.error("Error creating schema: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/{schemaId}/addUser")
    @JsonView(Views.Detailed.class)
    public SchemaDTO addUserToSchema(@Validated @RequestBody AddUserRequest request, @PathVariable  @NotNull long schemaId) {
        try {
            if (!Utility.isValidMobileNumber(request.getMobileNumber())) {
                throw new RuntimeException("Invalid mobile number");
            }
            LOG.info("Received add user request to add to schemaId:{}, for user:{}", schemaId, request.mobileNumber);
            return schemaService.addUserToSchema(schemaId, request.getMobileNumber(), request.getUserName());
        } catch (Exception e) {
            LOG.error("Error adding user to schema: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @GetMapping
    public List<Schema> getAllSchemas(){
        try{
            LOG.debug("Received request to fetch all schemas");
            return schemaService.getAllSchemas();
        } catch (Exception e){
            LOG.error("Error fetching schemas: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/{schemaId}/delete")
    public boolean deleteSchema(@PathVariable @NotNull long schemaId){
        try {
            LOG.info("Received delete schema request for schemaId: {}", schemaId);
            return schemaService.removeSchema(schemaId);
        } catch (Exception e) {
            LOG.error("Error deleting schema: {} ", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/{schemaId}/removeUser/{mobileNumber}")
    public boolean removeUserFromSchema(@PathVariable @NotNull long schemaId, @PathVariable @NotBlank String mobileNumber){
        try {
            if (!Utility.isValidMobileNumber(mobileNumber)) {
                throw new RuntimeException("Invalid mobile number");
            }
            LOG.info("Received remove user request to remove from schemaId:{}, for user:{}", schemaId, mobileNumber);
            return schemaService.removeUserFromSchema(schemaId, mobileNumber);
        } catch (Exception e) {
            LOG.error("Error removing user from schema: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Data
    static class AddUserRequest {
        @NotBlank(message = "Mobile number cannot be Empty")
        private String mobileNumber;
        @NotBlank(message = "User name cannot be Empty")
        private String userName;
    }
}
