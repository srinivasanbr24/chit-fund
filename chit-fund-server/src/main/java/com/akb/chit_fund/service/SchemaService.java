package com.akb.chit_fund.service;

import com.akb.chit_fund.dto.SchemaDTO;
import com.akb.chit_fund.model.Role;
import com.akb.chit_fund.model.Schema;
import com.akb.chit_fund.model.User;
import com.akb.chit_fund.repository.SchemaRepository;
import com.akb.chit_fund.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchemaService {

    private final SchemaRepository schemaRepo;
    private final UserRepository userRepo;
    private final UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(SchemaService.class);

    public SchemaService(SchemaRepository schemaRepo, UserRepository userRepo, UserService userService) {
        this.schemaRepo = schemaRepo;
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @Transactional
    public SchemaDTO createSchema(SchemaDTO schema) {
        try {
            LOG.info("Creating schema with name: {}", schema.getSchemaName());
            Schema schema1 = new Schema();
            schema1.setDescription(schema.getDescription());
            schema1.setName(schema.getSchemaName());
            schema1.setDurationInMonths(schema.getDurationInMonths());
            schema1.setMonthlyContribution(schema.getMonthlyContribution());
            schemaRepo.save(schema1);
            LOG.info("Schema created successfully with id: {}", schema1.getId());
            return schema;
        } catch (Exception e) {
            LOG.error("Error creating schema: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Transactional
    public SchemaDTO addUserToSchema(long id, String mobileNumber, String userName) {
        try {
            LOG.info("Adding user with mobile: {} to schemaId: {}", mobileNumber, id);
            if (StringUtils.isBlank(userName)) {
                throw new RuntimeException("User name cannot be empty");
            }
            Schema schema = schemaRepo.findById(id).orElseThrow(() -> new RuntimeException("No Such Schema Found"));

            if (isUserAssociatedWithSchema(schema, mobileNumber))
                throw new RuntimeException(" User already present in the Schema");

            User user = userRepo.findById(mobileNumber).isPresent() ?
                    userRepo.findById(mobileNumber).get() : userService.registerUser(mobileNumber, null, userName, Role.USER);

            schema.getUsers().add(user);
            user.getSchemas().add(schema);
            userRepo.save(user);
            schemaRepo.save(schema);
            LOG.info("User with Mobile number :{} added to schemaId: {} successfully", mobileNumber, id);
            return createDTO(schema);
        } catch (Exception e) {
            LOG.error("Error while adding user to schema: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public boolean removeSchema(@NotNull long schemaId) {
        try {
            LOG.info("Removing schema with id: {}", schemaId);
            Schema schema = schemaRepo.findById(schemaId).orElseThrow(() -> new RuntimeException("No Such Schema Found"));
            for (User user : schema.getUsers()) {
                user.getSchemas().remove(schema);
                userRepo.save(user);
            }
            schemaRepo.delete(schema);
            LOG.info("Schema with id: {} removed successfully", schemaId);
            return true;
        } catch (Exception e) {
            LOG.error("Error while removing schema: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public boolean removeUserFromSchema(@NotNull long schemaId, @NotBlank String mobileNumber) {
        try {
            LOG.info("Removing user with mobile: {} from schemaId: {}", mobileNumber, schemaId);
            Schema schema = schemaRepo.findById(schemaId).orElseThrow(() -> new RuntimeException("No Such Schema Found"));
            User user = userRepo.findById(mobileNumber).orElseThrow(() -> new RuntimeException("No Such User Found"));

            if (!isUserAssociatedWithSchema(schema, mobileNumber))
                throw new RuntimeException(" User not associated with the Schema");

            schema.getUsers().remove(user);
            user.getSchemas().remove(schema);
            userRepo.save(user);
            schemaRepo.save(schema);
            LOG.info("User with Mobile number :{} removed from schemaId: {} successfully", mobileNumber, schemaId);
            return true;
        } catch (Exception e) {
            LOG.error("Error while removing user from schema: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public List<Schema> getAllSchemas() {
        try {
            LOG.info("Retrieve all Schemas");
            return schemaRepo.findAll();
        } catch (Exception e) {
            LOG.error("Error while retrieving schemas: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private SchemaDTO createDTO(Schema schema) {
        SchemaDTO schemaDTO = new SchemaDTO();
        schemaDTO.setId(schema.getId());
        schemaDTO.setSchemaName(schema.getName());
        schemaDTO.setDescription(schema.getDescription());
        schemaDTO.setMonthlyContribution(schema.getMonthlyContribution());
        schemaDTO.setDurationInMonths(schema.getDurationInMonths());
        return schemaDTO;
    }


    private boolean isUserAssociatedWithSchema(Schema schema, String mobileNumber){
        return schema.getUsers().stream()
                .anyMatch(user -> user.getMobileNumber().equals(mobileNumber));
    }
    
}
