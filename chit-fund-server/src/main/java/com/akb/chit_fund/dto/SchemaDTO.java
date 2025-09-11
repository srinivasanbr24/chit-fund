package com.akb.chit_fund.dto;

import com.akb.chit_fund.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotBlank(message = "Schema name cannot be empty")
    private String schemaName;

    @JsonView(Views.Basic.class)
    @NotBlank(message = "Schema description cannot be empty")
    private String description;

    @JsonView(Views.Basic.class)
    @Positive(message = "Duration should be greater than 1 month")
    @NotNull(message = "Duration in months is required")
    private int durationInMonths;

    @JsonView(Views.Basic.class)
    @Positive(message = "Monthly contribution must be greater than 100")
    @NotNull(message = "Monthly contribution is required")
    private double monthlyContribution;

}
