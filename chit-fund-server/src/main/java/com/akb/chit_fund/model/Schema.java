package com.akb.chit_fund.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chit_fund_schemas")
public class Schema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    private int durationInMonths;

    private double monthlyContribution;

    @ManyToMany
    @JoinTable(
            name = "schema_user",
            joinColumns = @JoinColumn(name = "chit_fund_schemas_id"),
            inverseJoinColumns = @JoinColumn(name = "user_mobile_number")
    )
    private Set<User> users = new HashSet<>();

}
