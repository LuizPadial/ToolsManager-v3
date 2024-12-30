package com.Bio_Controle_Estoque.domain.repository;

import com.Bio_Controle_Estoque.domain.model.Tools;
import com.Bio_Controle_Estoque.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToolsRepository extends JpaRepository<Tools, Long> {
    //Busca exata:
    List<Tools> findByName(String name);

    //Busca usando palavra chave:
    List<Tools> findByNameContaining(String name);

    Optional<Tools> findByDescription(String description);

}
