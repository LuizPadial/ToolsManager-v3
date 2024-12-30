package com.Bio_Controle_Estoque.domain.repository;

import com.Bio_Controle_Estoque.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Busca exata:
    List<User> findByName(String name);

    //Busca usando palavra chave:
    List<User> findByNameContaining(String name);

    Optional<User> findByRegistration(String registration);

    List<User> findByNameContainingOrRegistrationContaining(String name, String registration);

    User findByUsername(String username);


    List<User> findByRegistrationContainingIgnoreCase(String registration);
}
