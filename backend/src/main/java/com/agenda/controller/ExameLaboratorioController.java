package com.agenda.controller;

import com.agenda.model.ExameLaboratorio;
import com.agenda.repository.ExameLaboratorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exames")
@RequiredArgsConstructor
public class ExameLaboratorioController {

    private final ExameLaboratorioRepository repository;

    @PostMapping
    public ResponseEntity<ExameLaboratorio> inserir(@RequestBody ExameLaboratorio exame) {
        return ResponseEntity.ok(repository.save(exame));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExameLaboratorio> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ExameLaboratorio>> listarTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExameLaboratorio> atualizar(@PathVariable Long id,
                                                       @RequestBody ExameLaboratorio dados) {
        return repository.findById(id).map(e -> {
            e.setDescricao(dados.getDescricao());
            e.setPosologia(dados.getPosologia());
            return ResponseEntity.ok(repository.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
