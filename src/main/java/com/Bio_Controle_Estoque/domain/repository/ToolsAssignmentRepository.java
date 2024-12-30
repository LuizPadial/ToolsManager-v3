package com.Bio_Controle_Estoque.domain.repository;

import com.Bio_Controle_Estoque.domain.model.ToolsAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ToolsAssignmentRepository extends JpaRepository<ToolsAssignment, Long> {


    // Buscar empréstimos ativos
    List<ToolsAssignment> findByReturnDateIsNull();

    // Buscar empréstimos ativos de um usuário específico
    List<ToolsAssignment> findByUserIdAndReturnDateIsNull(Long userId);

    // Buscar empréstimos de uma ferramenta específica
    List<ToolsAssignment> findByToolsId(Long toolId);

    // Histórico de empréstimos de um usuário
    List<ToolsAssignment> findByUserId(Long userId);

    // Histórico de empréstimos de uma ferramenta específica
    List<ToolsAssignment> findByToolsIdOrderByIssueDateDesc(Long toolId);

    Optional<ToolsAssignment> findById(Long id);

    // Verificar se uma ferramenta está emprestada no momento
    Optional<ToolsAssignment> findFirstByToolsIdAndReturnDateIsNull(Long toolId);



}
