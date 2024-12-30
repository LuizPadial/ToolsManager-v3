package com.Bio_Controle_Estoque.domain.service;

import com.Bio_Controle_Estoque.application.jwt.JwtService;
import com.Bio_Controle_Estoque.domain.AcessToken;
import com.Bio_Controle_Estoque.domain.exceptions.DuplicatedTupleException;
import com.Bio_Controle_Estoque.domain.model.User;
import com.Bio_Controle_Estoque.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public List<User> listarUsuarios() {
        return userRepository.findAll();
    }

    public List<User> buscarPorMatricula(String registration) {
        // Busca no campo de matrícula
        return userRepository.findByRegistrationContainingIgnoreCase(registration);
    }
    public User getByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User save(User user) {
        var possibleUser = getByUsername(user.getUsername());
        encodePassword(user);
        return userRepository.save(user);
    }

    public AcessToken authenticate(String username, String password) {
        var user = getByUsername(username);

        if (user == null) {
            return null;  // Se não encontrar o usuário, retorna null
        }

        boolean matches = passwordEncoder.matches(password, user.getPassword());

        if (matches) {
            // Verifica se o usuário é um manager
            if (!user.isManager()) {
                return null;  // Se não for manager, retorna null
            }

            // Gera o token caso seja manager
            return jwtService.generateToken(user);
        }

        return null;  // Se a senha não bater, retorna null
    }


    private void encodePassword(User user){
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
    }

    public Optional<User> atualizarUsuario(Long id, User userAtualizado) {
        return userRepository.findById(id).map(user -> {
            user.setName(userAtualizado.getName());
            user.setRegistration(userAtualizado.getRegistration());
            user.setUsername(userAtualizado.getUsername());
            user.setPassword(userAtualizado.getPassword());
            user.setBiometricData(userAtualizado.getBiometricData());
            user.setManager(userAtualizado.isManager());
            return userRepository.save(user);
        });
    }

    public Optional<User> atualizarUsuarioPorRegistration(String registration, User userAtualizado) {
        return userRepository.findByRegistration(registration).map(user -> {
            user.setName(userAtualizado.getName());
            user.setRegistration(userAtualizado.getRegistration());
            user.setUsername(userAtualizado.getUsername());
            user.setPassword(userAtualizado.getPassword());
            user.setBiometricData(userAtualizado.getBiometricData());
            return userRepository.save(user);
        });
    }


    public boolean deletarUsuario(Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }

    public Optional<User> buscarPorMatriculaUnico(String registration) {
        List<User> users = buscarPorMatricula(registration);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }


}