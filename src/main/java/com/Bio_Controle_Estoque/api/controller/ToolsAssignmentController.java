package com.Bio_Controle_Estoque.api.controller;

import com.Bio_Controle_Estoque.domain.DTO.ToolsAssignmentDTO;
import com.Bio_Controle_Estoque.domain.model.ToolsAssignment;
import com.Bio_Controle_Estoque.domain.service.ToolsAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/assignments")
public class ToolsAssignmentController {

    @Autowired
    private ToolsAssignmentService toolsAssignmentService;

    // Criar um novo empréstimo
    @PostMapping
    public ResponseEntity<ToolsAssignment> createAssignment(@RequestBody ToolsAssignment assignment) {
        return ResponseEntity.ok(toolsAssignmentService.createAssignment(assignment));
    }

    // Buscar todos os empréstimos
    @GetMapping
    public ResponseEntity<List<ToolsAssignment>> getAllAssignments() {
        return ResponseEntity.ok(toolsAssignmentService.getAllAssignments());
    }

    // Buscar um empréstimo pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<ToolsAssignmentDTO> getAssignmentById(@PathVariable Long id) {
        Optional<ToolsAssignment> assignment = toolsAssignmentService.getAssignmentById(id);
        return assignment
                .map(a -> ResponseEntity.ok(toolsAssignmentService.convertToDTO(a)))  // Chama o método de conversão da service
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ferramentas/{id}/historico")
    public ResponseEntity<List<ToolsAssignmentDTO>> getAssignmentHistory(@PathVariable Long id) {
        List<ToolsAssignmentDTO> assignments = toolsAssignmentService.getAssignmentHistoryByToolId(id);

        if (assignments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(assignments);
    }

    // Buscar empréstimos por usuário (com base no ID do usuário)
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<ToolsAssignmentDTO>> getAssignmentsByUserId(@PathVariable Long userId) {
        List<ToolsAssignmentDTO> assignments = toolsAssignmentService.getAssignmentsByUserId(userId);

        if (assignments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(assignments);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ToolsAssignment> updateAssignment(@PathVariable Long id, @RequestBody ToolsAssignment assignment) {
        if (!toolsAssignmentService.getAssignmentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        assignment.setId(id);
        return ResponseEntity.ok(toolsAssignmentService.updateAssignment(assignment));
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<ToolsAssignment> returnTool(@PathVariable Long id) {
        try {
            // Chama o serviço para realizar a devolução
            ToolsAssignment updatedAssignment = toolsAssignmentService.returnTool(id);
            return ResponseEntity.ok(updatedAssignment);
        } catch (RuntimeException e) {
            // Retorna erro 400 caso a ferramenta não possa ser devolvida
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}/returnData")
    public ResponseEntity<ToolsAssignment> returnToolData(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            // Recupera a data de devolução do corpo da requisição
            String returnDateStr = payload.get("returnDate");
            LocalDateTime returnDate = LocalDateTime.parse(returnDateStr);

            // Chama o serviço para realizar a devolução com a data
            ToolsAssignment updatedAssignment = toolsAssignmentService.returnToolData(id, returnDate);

            return ResponseEntity.ok(updatedAssignment);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        if (!toolsAssignmentService.getAssignmentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        toolsAssignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/open")
    public ResponseEntity<List<ToolsAssignment>> getOpenAssignments() {
        return ResponseEntity.ok(toolsAssignmentService.getOpenAssignments());
    }
}
