package com.akb.chit_fund.controller;

import com.akb.chit_fund.dto.SchemaDTO;
import com.akb.chit_fund.model.Schema;
import com.akb.chit_fund.service.SchemaService;
import com.akb.chit_fund.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/schemas")
public class SchemaController {

    private final SchemaService schemaService;


    public SchemaController(SchemaService schemaService) {
        this.schemaService = schemaService;
    }


    @PostMapping("/create")
    @JsonView(Views.Basic.class)
    public SchemaDTO createSchema(@RequestBody SchemaDTO schema) {
        System.out.println(" Schema DTO is"+schema);
       return schemaService.createSchema(schema);

    }

    @PostMapping("/{schemaId}/addUser")
    @JsonView(Views.Detailed.class)
    public SchemaDTO addUserToSchema(@RequestBody AddUserRequest request, @PathVariable Long schemaId) {
        return schemaService.addUserToSchema(schemaId, request.getMobileNumber(), request.getUserName());
    }

    @GetMapping
    public List<Schema> getAllSchemas(){
        return schemaService.getAllSchemas();
    }


    @Data
    static class AddUserRequest {
        private String mobileNumber;
        private String userName;
    }
}
