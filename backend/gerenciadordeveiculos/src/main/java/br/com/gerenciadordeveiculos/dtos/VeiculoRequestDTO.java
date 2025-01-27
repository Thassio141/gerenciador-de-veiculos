package br.com.gerenciadordeveiculos.dtos;

import br.com.gerenciadordeveiculos.enums.TipoCombustivel;
import br.com.gerenciadordeveiculos.enums.TipoVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VeiculoRequestDTO {

    @NotNull
    private TipoVeiculo tipo;

    @NotBlank
    private String modelo;

    @NotBlank
    private String fabricante;

    @NotNull
    private Integer ano;

    @NotNull
    private Double preco;

    private String cor;

    private Integer quantidadePortas;

    private TipoCombustivel tipoCombustivel;

    private Integer cilindrada;
}
