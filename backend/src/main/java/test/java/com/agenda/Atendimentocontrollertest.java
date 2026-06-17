package com.agenda.controller;

import com.agenda.model.Atendimento;
import com.agenda.model.enums.TipoReceita;
import com.agenda.repository.AtendimentoRepository;
import com.agenda.service.VideoConferenciaService;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtendimentoController")
class AtendimentoControllerTest {

    @Mock
    private AtendimentoRepository repository;

    @Mock
    private VideoConferenciaService videoService;

    @InjectMocks
    private AtendimentoController controller;

    private Atendimento atendimento;

    @BeforeEach
    void setUp() {
        atendimento = Atendimento.builder()
                .id(1L)
                .titulo("Consulta Inicial")
                .data(LocalDate.of(2025, 8, 10))
                .horario(LocalTime.of(14, 30))
                .receita(TipoReceita.REMEDIO)
                .linkVideoconferencia("https://meet.exemplo.com/consulta-inicial")
                .build();
    }

    @Nested
    @DisplayName("POST /atendimentos")
    class Criar {

        @Test
        @DisplayName("deve criar atendimento, gerar link de vídeo e retornar 200")
        void deveCriarComLinkDeVideo() {
            String linkEsperado = "https://meet.exemplo.com/consulta-inicial-123";
            when(videoService.gerarLinkVideoconferencia(any())).thenReturn(linkEsperado);
            when(repository.save(any(Atendimento.class))).thenReturn(atendimento);

            ResponseEntity<Atendimento> response = controller.criar(atendimento);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            // Verifica que o serviço de vídeo foi chamado antes de salvar
            verify(videoService).gerarLinkVideoconferencia(atendimento);
            verify(repository).save(atendimento);
        }

        @Test
        @DisplayName("deve setar o link de vídeo no atendimento antes de salvar")
        void deveSetarLinkNoAtendimento() {
            String linkEsperado = "https://meet.exemplo.com/link-gerado";
            Atendimento novoAtendimento = Atendimento.builder()
                    .titulo("Nova Consulta")
                    .data(LocalDate.now())
                    .horario(LocalTime.of(10, 0))
                    .build();

            when(videoService.gerarLinkVideoconferencia(novoAtendimento)).thenReturn(linkEsperado);
            when(repository.save(novoAtendimento)).thenReturn(novoAtendimento);

            controller.criar(novoAtendimento);

            // Verifica que o link foi setado no objeto antes do save
            assertThat(novoAtendimento.getLinkVideoconferencia()).isEqualTo(linkEsperado);
        }
    }

    @Nested
    @DisplayName("GET /atendimentos/{id}")
    class BuscarPorId {

        @Test
        @DisplayName("deve retornar 200 com atendimento quando encontrado")
        void deveRetornarAtendimento() {
            when(repository.findById(1L)).thenReturn(Optional.of(atendimento));

            ResponseEntity<Atendimento> response = controller.buscarPorId(1L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(atendimento);
        }

        @Test
        @DisplayName("deve retornar 404 quando não encontrado")
        void deveRetornar404() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            ResponseEntity<Atendimento> response = controller.buscarPorId(99L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("GET /atendimentos")
    class ListarTodos {

        @Test
        @DisplayName("deve retornar lista com todos os atendimentos")
        void deveListarTodos() {
            Atendimento segundo = Atendimento.builder().id(2L).titulo("Retorno").build();
            when(repository.findAll()).thenReturn(List.of(atendimento, segundo));

            ResponseEntity<List<Atendimento>> response = controller.listarTodos();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(2);
        }

        @Test
        @DisplayName("deve retornar lista vazia quando não há atendimentos")
        void deveRetornarListaVazia() {
            when(repository.findAll()).thenReturn(List.of());

            ResponseEntity<List<Atendimento>> response = controller.listarTodos();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();
        }
    }

    @Nested
    @DisplayName("PUT /atendimentos/{id}")
    class Atualizar {

        @Test
        @DisplayName("deve atualizar e retornar 200 quando atendimento existe")
        void deveAtualizarComSucesso() {
            Atendimento dadosNovos = Atendimento.builder()
                    .titulo("Consulta Atualizada")
                    .data(LocalDate.of(2025, 9, 1))
                    .horario(LocalTime.of(9, 0))
                    .receita(TipoReceita.ATIVIDADE_FISICA)
                    .build();

            when(repository.findById(1L)).thenReturn(Optional.of(atendimento));
            when(repository.save(any())).thenReturn(atendimento);

            ResponseEntity<Atendimento> response = controller.atualizar(1L, dadosNovos);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(repository).save(atendimento);
        }

        @Test
        @DisplayName("deve retornar 404 ao atualizar atendimento inexistente")
        void deveRetornar404QuandoNaoEncontrado() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            ResponseEntity<Atendimento> response = controller.atualizar(99L, atendimento);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("DELETE /atendimentos/{id}")
    class Excluir {

        @Test
        @DisplayName("deve excluir e retornar 204 quando atendimento existe")
        void deveExcluirComSucesso() {
            when(repository.existsById(1L)).thenReturn(true);
            doNothing().when(repository).deleteById(1L);

            ResponseEntity<Void> response = controller.excluir(1L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            verify(repository).deleteById(1L);
        }

        @Test
        @DisplayName("deve retornar 404 ao excluir atendimento inexistente")
        void deveRetornar404QuandoNaoExiste() {
            when(repository.existsById(99L)).thenReturn(false);

            ResponseEntity<Void> response = controller.excluir(99L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            verify(repository, never()).deleteById(any());
        }
    }
}