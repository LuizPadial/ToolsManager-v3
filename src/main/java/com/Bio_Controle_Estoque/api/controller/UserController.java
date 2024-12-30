package com.Bio_Controle_Estoque.api.controller;

import com.Bio_Controle_Estoque.domain.DTO.CredentialsDTO;
import com.Bio_Controle_Estoque.domain.service.UserService;
import com.Bio_Controle_Estoque.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> listar() {
        return userService.listarUsuarios();
    }

    @GetMapping("/registration/{registration}") // O caminho agora usa a matrícula como parâmetro na URL
    public ResponseEntity<List<User>> buscarPorMatricula(@PathVariable String registration) {
        if (registration == null || registration.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Retorna bad request caso não passe matrícula
        }

        List<User> users = userService.buscarPorMatricula(registration);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna noContent se não encontrar usuários
        }

        return ResponseEntity.ok(users); // Retorna os usuários encontrados
    }

    @PostMapping
    public User cadastrar(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> atualizar(@PathVariable Long userId, @RequestBody User user) {
        Optional<User> usuarioAtualizado = userService.atualizarUsuario(userId, user);
        return usuarioAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = userService.deletarUsuario(id);
        return deletado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/auth")
    public ResponseEntity autheticate(@RequestBody CredentialsDTO credentials){
        var token = userService.authenticate(credentials.getUsername(), credentials.getPassword());

        if(token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(token);
    }

}
