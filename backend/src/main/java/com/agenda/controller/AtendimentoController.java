package com.agenda.controller;

import com.agenda.model.Atendimento;
import com.agenda.repository.AtendimentoRepository;
import com.agenda.service.VideoConferenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atendimentos")
@RequiredArgsConstructor
public class AtendimentoController {

    private final AtendimentoRepository repository;
    private final VideoConferenciaService videoService;

    // Criar atendimento e gerar link de videoconferência
    @PostMapping
    public ResponseEntity<Atendimento> criar(@RequestBody Atendimento atendimento) {
        // Integração com API de vídeo (ex: Google Meet, Zoom)
        String link = videoService.gerarLinkVideoconferencia(atendimento);
        atendimento.setLinkVideoconferencia(link);
        return ResponseEntity.ok(repository.save(atendimento));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atendimento> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Atendimento>> listarTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Atendimento> atualizar(@PathVariable Long id,
                                                  @RequestBody Atendimento dados) {
        return repository.findById(id).map(a -> {
            a.setData(dados.getData());
            a.setHorario(dados.getHorario());
            a.setTitulo(dados.getTitulo());
            a.setReceita(dados.getReceita());
            return ResponseEntity.ok(repository.save(a));
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
