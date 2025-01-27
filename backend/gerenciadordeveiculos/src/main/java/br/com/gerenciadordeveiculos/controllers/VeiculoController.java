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
    public ResponseEntity<List<VeiculoResponseDTO>> buscarVeiculos(
            @RequestParam(required = false) TipoVeiculo tipo,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String cor,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        List<VeiculoResponseDTO> lista = veiculoService.buscarVeiculosPaginado(
                page, size, tipo, modelo, ano, cor
        );
        return ResponseEntity.ok(lista);
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
