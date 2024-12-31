package com.Bio_Controle_Estoque.domain.model;

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
@AllArgsConstructor
@NoArgsConstructor
public class Tools {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @Getter
    @Column(name = "available")  // Mapeando a coluna is_available
    private boolean available;

    @OneToMany(mappedBy = "tools", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToolsAssignment> assignments = new ArrayList<>();


    public boolean getAvailable() {
        return available;
    }

}
