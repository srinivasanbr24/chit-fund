package com.akb.chit_fund.dto;

import com.akb.chit_fund.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchemaDTO {

    @JsonView(Views.Detailed.class)
    private long id;

    @JsonView({Views.Basic.class})
    private String schemaName;

    @JsonView(Views.Basic.class)
    private String description;

    @JsonView(Views.Basic.class)
    private Integer durationInMonths;

    @JsonView(Views.Basic.class)
    private Double amount;

}
