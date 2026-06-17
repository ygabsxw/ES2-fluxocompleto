package com.agenda.controller;

import com.agenda.model.ProfissionalSaude;
import com.agenda.model.enums.Categoria;
import com.agenda.repository.ProfissionalSaudeRepository;
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
@DisplayName("ProfissionalSaudeController")
class ProfissionalSaudeControllerTest {

    @Mock
    private ProfissionalSaudeRepository repository;

    @InjectMocks
    private ProfissionalSaudeController controller;

    private ProfissionalSaude profissional;

    @BeforeEach
    void setUp() {
        profissional = ProfissionalSaude.builder()
                .id(1L)
                .nome("Dr. Carlos")
                .telefone("31999999999")
                .endereco("Rua das Flores, 100")
                .categoria(Categoria.MEDICO)
                .build();
    }

    // -------------------------------------------------------------------------
    // POST - inserir
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("POST /profissionais")
    class Inserir {

        @Test
        @DisplayName("deve inserir e retornar 200 com o profissional salvo")
        void deveInserirComSucesso() {
            when(repository.save(any(ProfissionalSaude.class))).thenReturn(profissional);

            ResponseEntity<ProfissionalSaude> response = controller.inserir(profissional);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getNome()).isEqualTo("Dr. Carlos");
            verify(repository, times(1)).save(profissional);
        }
    }

    // -------------------------------------------------------------------------
    // PUT - alterar
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("PUT /profissionais/{id}")
    class Alterar {

        @Test
        @DisplayName("deve alterar e retornar 200 quando profissional existe")
        void deveAlterarComSucesso() {
            ProfissionalSaude dadosNovos = ProfissionalSaude.builder()
                    .nome("Dr. Carlos Atualizado")
                    .telefone("31988888888")
                    .endereco("Av. Principal, 200")
                    .categoria(Categoria.FISIOTERAPEUTA)
                    .build();

            when(repository.findById(1L)).thenReturn(Optional.of(profissional));
            when(repository.save(any(ProfissionalSaude.class))).thenReturn(profissional);

            ResponseEntity<ProfissionalSaude> response = controller.alterar(1L, dadosNovos);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(repository).findById(1L);
            verify(repository).save(profissional);
        }

        @Test
        @DisplayName("deve retornar 404 quando profissional não existe")
        void deveRetornar404QuandoNaoEncontrado() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            ResponseEntity<ProfissionalSaude> response = controller.alterar(99L, profissional);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            verify(repository, never()).save(any());
        }
    }

    // -------------------------------------------------------------------------
    // GET - consultar por id
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("GET /profissionais/{id}")
    class ConsultarPorId {

        @Test
        @DisplayName("deve retornar 200 com o profissional quando encontrado")
        void deveRetornarProfissionalQuandoEncontrado() {
            when(repository.findById(1L)).thenReturn(Optional.of(profissional));

            ResponseEntity<ProfissionalSaude> response = controller.consultarPorId(1L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(profissional);
        }

        @Test
        @DisplayName("deve retornar 404 quando não encontrado")
        void deveRetornar404QuandoNaoEncontrado() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            ResponseEntity<ProfissionalSaude> response = controller.consultarPorId(99L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // -------------------------------------------------------------------------
    // GET - consultar por nome
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("GET /profissionais/buscar/nome")
    class ConsultarPorNome {

        @Test
        @DisplayName("deve retornar lista de profissionais pelo nome")
        void deveRetornarPorNome() {
            when(repository.findByNomeContainingIgnoreCase("carlos"))
                    .thenReturn(List.of(profissional));

            ResponseEntity<List<ProfissionalSaude>> response = controller.consultarPorNome("carlos");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(1);
            assertThat(response.getBody().get(0).getNome()).isEqualTo("Dr. Carlos");
        }

        @Test
        @DisplayName("deve retornar lista vazia quando nenhum nome bate")
        void deveRetornarListaVazia() {
            when(repository.findByNomeContainingIgnoreCase("xyz")).thenReturn(List.of());

            ResponseEntity<List<ProfissionalSaude>> response = controller.consultarPorNome("xyz");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();
        }
    }

    // -------------------------------------------------------------------------
    // GET - consultar por categoria
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("GET /profissionais/buscar/categoria")
    class ConsultarPorCategoria {

        @Test
        @DisplayName("deve retornar profissionais pela categoria")
        void deveRetornarPorCategoria() {
            when(repository.findByCategoria(Categoria.MEDICO)).thenReturn(List.of(profissional));

            ResponseEntity<List<ProfissionalSaude>> response =
                    controller.consultarPorCategoria(Categoria.MEDICO);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(1);
            assertThat(response.getBody().get(0).getCategoria()).isEqualTo(Categoria.MEDICO);
        }
    }

    // -------------------------------------------------------------------------
    // DELETE - excluir
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("DELETE /profissionais/{id}")
    class Excluir {

        @Test
        @DisplayName("deve excluir e retornar 204 quando profissional existe")
        void deveExcluirComSucesso() {
            when(repository.existsById(1L)).thenReturn(true);
            doNothing().when(repository).deleteById(1L);

            ResponseEntity<Void> response = controller.excluir(1L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            verify(repository).deleteById(1L);
        }

        @Test
        @DisplayName("deve retornar 404 ao tentar excluir profissional inexistente")
        void deveRetornar404QuandoNaoExiste() {
            when(repository.existsById(99L)).thenReturn(false);

            ResponseEntity<Void> response = controller.excluir(99L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            verify(repository, never()).deleteById(any());
        }
    }
}