package br.com.gerenciadordeveiculos.controllers;

import br.com.gerenciadordeveiculos.dtos.VeiculoRequestDTO;
import br.com.gerenciadordeveiculos.dtos.VeiculoResponseDTO;
import br.com.gerenciadordeveiculos.enums.TipoVeiculo;
import br.com.gerenciadordeveiculos.services.VeiculoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
@CrossOrigin(origins = "*")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @PostMapping
    public ResponseEntity<Void> criarVeiculo(@RequestBody VeiculoRequestDTO dto) {
        veiculoService.criarVeiculo(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<VeiculoResponseDTO>> buscarTodos(
            @RequestParam(required = false) TipoVeiculo tipo,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String cor
    ) {
        if (tipo != null || modelo != null || ano != null || (cor != null && !cor.isEmpty())) {
            List<VeiculoResponseDTO> filtrados = veiculoService.buscarPorFiltros(tipo, modelo, ano, cor);
            return ResponseEntity.ok(filtrados);
        } else {
            List<VeiculoResponseDTO> todos = veiculoService.buscarTodos();
            return ResponseEntity.ok(todos);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> buscarPorId(@PathVariable Integer id) {
        VeiculoResponseDTO dto = veiculoService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarVeiculo(@PathVariable Integer id, @RequestBody VeiculoRequestDTO dto) {
        veiculoService.atualizarVeiculo(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable Integer id) {
        veiculoService.deletarVeiculo(id);
        return ResponseEntity.noContent().build();
    }
}
