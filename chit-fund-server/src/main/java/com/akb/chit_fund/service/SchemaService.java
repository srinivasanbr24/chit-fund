package com.akb.chit_fund.service;

import com.akb.chit_fund.dto.SchemaDTO;
import com.akb.chit_fund.model.Role;
import com.akb.chit_fund.model.Schema;
import com.akb.chit_fund.model.User;
import com.akb.chit_fund.repository.SchemaRepository;
import com.akb.chit_fund.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchemaService {

    private final SchemaRepository schemaRepo;
    private final UserRepository userRepo;
    private final UserService userService;

    public SchemaService(SchemaRepository schemaRepo, UserRepository userRepo, UserService userService) {
        this.schemaRepo = schemaRepo;
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @Transactional
    public SchemaDTO createSchema(SchemaDTO schema) {
        Schema schema1 = new Schema();
        schema1.setDescription(schema.getDescription());
        schema1.setName(schema.getSchemaName());
        schema1.setDurationInMonths(schema.getDurationInMonths());
        schema1.setMonthlyContribution(schema.getMonthlyContribution());
        schemaRepo.save(schema1);
        System.out.println(" schema is added");
        return schema;
    }

    // Need to check the existance of the user in the schema,
    // if not only we need to add or else need to create the new user with role as user.
    @Transactional
    public SchemaDTO addUserToSchema(Long id, String mobileNumber, String userName) {
        Schema schema = schemaRepo.findById(id).orElseThrow(()-> new RuntimeException("No Such Schema Found"));
        User user = null;

      if(userRepo.findById(mobileNumber).isPresent()){
          user = userRepo.findById(mobileNumber).get();
      } else {
          user = userService.registerUser(mobileNumber,null, userName, Role.USER);
      }

     if(isUserAssociatedWithSchema(schema,mobileNumber)) throw new RuntimeException(" User already present in the Schema");

        schema.getUsers().add(user);
        user.getSchemas().add(schema);
        userRepo.save(user);
        schemaRepo.save(schema);
        return createDTO(schema);
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

    public List<Schema> getAllSchemas() {
        return schemaRepo.findAll();
    }

    private boolean isUserAssociatedWithSchema(Schema schema, String mobileNumber){
        return schema.getUsers().stream()
                .anyMatch(user -> user.getMobileNumber().equals(mobileNumber));
    }

}
