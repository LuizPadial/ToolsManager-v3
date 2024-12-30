package com.Bio_Controle_Estoque.domain.service;

import com.Bio_Controle_Estoque.domain.DTO.ToolAvailabilityDTO;
import com.Bio_Controle_Estoque.domain.model.Tools;
import com.Bio_Controle_Estoque.domain.model.ToolsAssignment;
import com.Bio_Controle_Estoque.domain.model.User;
import com.Bio_Controle_Estoque.domain.repository.ToolsRepository;
import com.Bio_Controle_Estoque.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ToolsService {

    @Autowired
    private ToolsRepository toolsRepository;

    public List<Tools> listarFerramentas() {
        return toolsRepository.findAll();
    }

    public Optional<Tools> buscarFerramentaPorId(Long id) {
        return toolsRepository.findById(id);
    }

    public List<Tools> buscarPorNomeOuCodigo(String query) {
        return toolsRepository.findByNameContaining(query);
    }


    public Tools salvarFerramenta(Tools tools) {
        return toolsRepository.save(tools);
    }

    public Optional<Tools> atualizarFerramenta(Long id, Tools toolsAtualizado) {
        return toolsRepository.findById(id).map(tools -> {
            tools.setName(toolsAtualizado.getName());
            tools.setDescription(toolsAtualizado.getDescription());
            return toolsRepository.save(tools);
        });
    }

    public boolean deletarFerramenta(Long id) {
        return toolsRepository.findById(id).map(tools -> {
            toolsRepository.delete(tools);
            return true;
        }).orElse(false);
    }

    public Optional<Tools> updateAvailability(Long toolId, ToolAvailabilityDTO toolAvailabilityDTO) {
        Optional<Tools> toolOptional = toolsRepository.findById(toolId);
        if (toolOptional.isPresent()) {
            Tools tool = toolOptional.get();
            tool.setAvailable(toolAvailabilityDTO.isAvailable());  // Atualiza apenas o campo 'available'
            toolsRepository.save(tool);  // Salva no banco de dados
            return Optional.of(tool);
        }
        return Optional.empty(); // Retorna empty se não encontrar a ferramenta
    }




}