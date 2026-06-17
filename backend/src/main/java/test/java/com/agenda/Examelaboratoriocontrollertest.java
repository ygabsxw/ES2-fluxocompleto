package com.agenda.controller;

import com.agenda.model.ExameLaboratorio;
import com.agenda.repository.ExameLaboratorioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExameLaboratorioController")
class ExameLaboratorioControllerTest {

    @Mock
    private ExameLaboratorioRepository repository;

    @InjectMocks
    private ExameLaboratorioController controller;

    private ExameLaboratorio exame;

    @BeforeEach
    void setUp() {
        exame = ExameLaboratorio.builder()
                .id(1L)
                .descricao("Hemograma Completo")
                .posologia("Jejum de 8 horas")
                .build();
    }

    @Nested
    @DisplayName("POST /exames")
    class Inserir {

        @Test
        @DisplayName("deve inserir e retornar 200 com o exame salvo")
        void deveInserirComSucesso() {
            when(repository.save(any(ExameLaboratorio.class))).thenReturn(exame);

            ResponseEntity<ExameLaboratorio> response = controller.inserir(exame);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getDescricao()).isEqualTo("Hemograma Completo");
            verify(repository).save(exame);
        }
    }

    @Nested
    @DisplayName("GET /exames/{id}")
    class BuscarPorId {

        @Test
        @DisplayName("deve retornar 200 com o exame quando encontrado")
        void deveRetornarExame() {
            when(repository.findById(1L)).thenReturn(Optional.of(exame));

            ResponseEntity<ExameLaboratorio> response = controller.buscarPorId(1L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(exame);
        }

        @Test
        @DisplayName("deve retornar 404 quando exame não encontrado")
        void deveRetornar404() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            ResponseEntity<ExameLaboratorio> response = controller.buscarPorId(99L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("GET /exames")
    class ListarTodos {

        @Test
        @DisplayName("deve retornar todos os exames")
        void deveListarTodos() {
            ExameLaboratorio segundo = ExameLaboratorio.builder()
                    .id(2L).descricao("Glicemia").posologia("Jejum de 12 horas").build();
            when(repository.findAll()).thenReturn(List.of(exame, segundo));

            ResponseEntity<List<ExameLaboratorio>> response = controller.listarTodos();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("PUT /exames/{id}")
    class Atualizar {

        @Test
        @DisplayName("deve atualizar e retornar 200 quando exame existe")
        void deveAtualizarComSucesso() {
            ExameLaboratorio dadosNovos = ExameLaboratorio.builder()
                    .descricao("Hemograma Atualizado")
                    .posologia("Sem jejum")
                    .build();

            when(repository.findById(1L)).thenReturn(Optional.of(exame));
            when(repository.save(any())).thenReturn(exame);

            ResponseEntity<ExameLaboratorio> response = controller.atualizar(1L, dadosNovos);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(repository).save(exame);
        }

        @Test
        @DisplayName("deve retornar 404 ao atualizar exame inexistente")
        void deveRetornar404() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            ResponseEntity<ExameLaboratorio> response = controller.atualizar(99L, exame);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("DELETE /exames/{id}")
    class Excluir {

        @Test
        @DisplayName("deve excluir e retornar 204 quando exame existe")
        void deveExcluirComSucesso() {
            when(repository.existsById(1L)).thenReturn(true);
            doNothing().when(repository).deleteById(1L);

            ResponseEntity<Void> response = controller.excluir(1L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            verify(repository).deleteById(1L);
        }

        @Test
        @DisplayName("deve retornar 404 ao excluir exame inexistente")
        void deveRetornar404() {
            when(repository.existsById(99L)).thenReturn(false);

            ResponseEntity<Void> response = controller.excluir(99L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            verify(repository, never()).deleteById(any());
        }
    }
}