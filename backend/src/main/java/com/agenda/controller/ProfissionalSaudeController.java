package com.agenda.controller;

import com.agenda.model.ProfissionalSaude;
import com.agenda.repository.ProfissionalSaudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*") // Para permitir requisições do Frontend em React
public class ProfissionalSaudeController {

    @Autowired
    private ProfissionalSaudeRepository repository;

    // Inserir()
    @PostMapping
    public ProfissionalSaude inserir(@RequestBody ProfissionalSaude profissional) {
        return repository.save(profissional);
    }

    // Alterar(id)
    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalSaude> alterar(@PathVariable Long id, @RequestBody ProfissionalSaude detalhes) {
        return repository.findById(id)
                .map(profissional -> {
                    profissional.setNome(detalhes.getNome());
                    profissional.setTelefone(detalhes.getTelefone());
                    profissional.setEndereco(detalhes.getEndereco());
                    profissional.setCategorias(detalhes.getCategorias());
                    return ResponseEntity.ok(repository.save(profissional));
                }).orElse(ResponseEntity.notFound().build());
    }

    // Consultar(id)
    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalSaude> consultarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Consultar(nome)
    @GetMapping("/buscar/nome")
    public List<ProfissionalSaude> consultarPorNome(@RequestParam String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    // Consultar(categoria)
    @GetMapping("/buscar/categoria")
    public List<ProfissionalSaude> consultarPorCategoria(@RequestParam String categoria) {
        return repository.findByCategoriasContainingIgnoreCase(categoria);
    }

    // Excluir(id)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return repository.findById(id)
                .map(profissional -> {
                    repository.delete(profissional);
                    return ResponseEntity.ok().<Void>build();
                }).orElse(ResponseEntity.notFound().build());
    }
}