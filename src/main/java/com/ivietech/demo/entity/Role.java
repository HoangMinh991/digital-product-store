package com.ivietech.demo.entity;

import javax.persistence.*;
import java.util.Set;
import lombok.Data;

@Entity
@Table(name = "role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Set<Accounts> accounts;

}

    
