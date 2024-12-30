package com.Bio_Controle_Estoque.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ToolsAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-tools")
    private User user;


    @ManyToOne
    @JoinColumn(name = "tool_id", nullable = false)
    @JsonBackReference
    private Tools tools;

    private LocalDateTime issueDate;
    private LocalDateTime returnDate;

}
