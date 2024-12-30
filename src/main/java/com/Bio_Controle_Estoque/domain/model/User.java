package com.Bio_Controle_Estoque.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String registration;
    private String username;
    private String password;
    private boolean isManager;
    private String biometricData;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference("user-tools")
    private List<ToolsAssignment> toolsAssignments;

}

