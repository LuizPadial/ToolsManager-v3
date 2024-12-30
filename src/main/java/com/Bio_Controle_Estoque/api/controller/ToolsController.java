package com.Bio_Controle_Estoque.api.controller;

import com.Bio_Controle_Estoque.domain.DTO.ToolAvailabilityDTO;
import com.Bio_Controle_Estoque.domain.model.Tools;
import com.Bio_Controle_Estoque.domain.service.ToolsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/ferramentas")
public class ToolsController {

    private final ToolsService ToolsService;

    @GetMapping
    public List<Tools> listar() {
        return ToolsService.listarFerramentas();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Tools>> buscarPorNomeOuCodigo(@RequestParam String query) {
        List<Tools> Toolss = ToolsService.buscarPorNomeOuCodigo(query);

        if (Toolss.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 caso não encontre resultados
        }
        return ResponseEntity.ok(Toolss); // Retorna 200 OK com os usuários encontrados
    }

    @PostMapping
    public Tools cadastrar(@RequestBody Tools Tools) {
        return ToolsService.salvarFerramenta(Tools);
    }

    @PutMapping("/{ToolsId}")
    public ResponseEntity<Tools> atualizar(@PathVariable Long ToolsId, @RequestBody Tools Tools) {
        Optional<Tools> usuarioAtualizado = ToolsService.atualizarFerramenta(ToolsId, Tools);
        return usuarioAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/status/{ToolsId}")
    public ResponseEntity<Tools> atualizarDisponibilidade(@PathVariable Long ToolsId, @RequestBody ToolAvailabilityDTO toolAvailabilityDTO) {
        System.out.println("Atualizando disponibilidade para ferramenta com ID: " + ToolsId);
        System.out.println("Disponibilidade recebida: " + toolAvailabilityDTO.isAvailable());

        Optional<Tools> updatedTool = ToolsService.updateAvailability(ToolsId, toolAvailabilityDTO);
        return updatedTool.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = ToolsService.deletarFerramenta(id);
        return deletado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
