package com.Bio_Controle_Estoque.domain.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ToolsAssignmentDTO {


    private Long id;
    private UserDTO user;
    private ToolsDTO tools;
    private LocalDateTime issueDate;
    private LocalDateTime returnDate;



}
