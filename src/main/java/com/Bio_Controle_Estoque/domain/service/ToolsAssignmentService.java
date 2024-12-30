package com.Bio_Controle_Estoque.domain.service;

import com.Bio_Controle_Estoque.domain.DTO.ToolsAssignmentDTO;
import com.Bio_Controle_Estoque.domain.DTO.ToolsDTO;
import com.Bio_Controle_Estoque.domain.DTO.UserDTO;
import com.Bio_Controle_Estoque.domain.model.Tools;
import com.Bio_Controle_Estoque.domain.model.ToolsAssignment;
import com.Bio_Controle_Estoque.domain.repository.ToolsAssignmentRepository;
import com.Bio_Controle_Estoque.domain.repository.ToolsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToolsAssignmentService {

    @Autowired
    private ToolsAssignmentRepository toolsAssignmentRepository;

    @Autowired
    private ToolsRepository toolsRepository;

    // Criar um novo registro de empréstimo com verificação de disponibilidade da ferramenta
    public ToolsAssignment createAssignment(ToolsAssignment assignment) {
        // Buscar a ferramenta
        Tools tool = toolsRepository.findById(assignment.getTools().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ferramenta não encontrada"));

        // Verificar se a ferramenta já está emprestada
        if (!tool.isAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ferramenta já emprestada");
        }

        // Marcar a ferramenta como indisponível
        tool.setAvailable(false);
        toolsRepository.save(tool);

        // Salvar o empréstimo
        return toolsAssignmentRepository.save(assignment);
    }

    //Buscar os emprestimos do usuário
    public List<ToolsAssignmentDTO> getAssignmentsByUserId(Long userId) {
        List<ToolsAssignment> assignments = toolsAssignmentRepository.findByUserId(userId);

        // Converter para DTO se necessário
        return assignments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    // Atualizar o empréstimo de ferramenta com a devolução
    @Transactional
    public ToolsAssignment returnTool(Long id) {
        // Buscar o empréstimo
        ToolsAssignment assignment = toolsAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        // Verificar se a ferramenta já foi devolvida
        if (assignment.getReturnDate() != null) {
            throw new RuntimeException("Ferramenta já devolvida");
        }

        // Buscar a ferramenta associada ao empréstimo
        Tools tool = assignment.getTools();
        tool.setAvailable(true); // Marcar a ferramenta como disponível

        // Definir a data de devolução
        assignment.setReturnDate(LocalDateTime.now());

        // Salvar as alterações
        toolsRepository.save(tool); // Salva a ferramenta com a nova disponibilidade
        return toolsAssignmentRepository.save(assignment); // Salva o empréstimo com a data de devolução
    }

    public ToolsAssignment returnToolData(Long id, LocalDateTime returnDate) {
        // Encontre o empréstimo da ferramenta
        ToolsAssignment assignment = toolsAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        // Atualize a data de devolução
        assignment.setReturnDate(LocalDateTime.now()); // Definindo a data de devolução para o momento atual

        // Salve as alterações
        return toolsAssignmentRepository.save(assignment);
    }

    // Serviço que retorna o histórico de ferramentas mapeado para DTOs
    public List<ToolsAssignmentDTO> getAssignmentHistoryByToolId(Long toolId) {
        List<ToolsAssignment> assignments = toolsAssignmentRepository.findByToolsId(toolId);

        return assignments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar todos os registros
    public List<ToolsAssignment> getAllAssignments() {
        return toolsAssignmentRepository.findAll();
    }

    // Buscar um registro por ID
    public Optional<ToolsAssignment> getAssignmentById(Long id) {
        return toolsAssignmentRepository.findById(id);
    }

    // Atualizar um registro
    public ToolsAssignment updateAssignment(ToolsAssignment assignment) {
        return toolsAssignmentRepository.save(assignment);
    }

    // Deletar um registro
    public void deleteAssignment(Long id) {
        toolsAssignmentRepository.deleteById(id);
    }

    // Buscar ferramentas emprestadas não devolvidas
    public List<ToolsAssignment> getOpenAssignments() {
        return toolsAssignmentRepository.findByReturnDateIsNull();
    }

    // Método de conversão para DTO
    public ToolsAssignmentDTO convertToDTO(ToolsAssignment toolsAssignment) {
        ToolsAssignmentDTO dto = new ToolsAssignmentDTO();
        dto.setId(toolsAssignment.getId());
        dto.setIssueDate(toolsAssignment.getIssueDate());
        dto.setReturnDate(toolsAssignment.getReturnDate());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(toolsAssignment.getUser().getId());
        userDTO.setUsername(toolsAssignment.getUser().getUsername());
        userDTO.setPassword(toolsAssignment.getUser().getPassword());
        dto.setUser(userDTO);

        ToolsDTO toolsDTO = new ToolsDTO();
        toolsDTO.setId(toolsAssignment.getTools().getId());
        toolsDTO.setName(toolsAssignment.getTools().getName());
        dto.setTools(toolsDTO);

        return dto;
    }


}
