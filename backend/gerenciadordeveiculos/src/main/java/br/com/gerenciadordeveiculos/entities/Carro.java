package br.com.gerenciadordeveiculos.entities;

import br.com.gerenciadordeveiculos.enums.TipoCombustivel;
import br.com.gerenciadordeveiculos.enums.TipoVeiculo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Carro extends Veiculo {

    private Integer quantidadePortas;

    private TipoCombustivel tipoCombustivel;

    public Carro(
            Integer id,
            TipoVeiculo tipo,
            String modelo,
            String fabricante,
            Integer ano,
            Double preco,
            String cor,
            Integer quantidadePortas,
            TipoCombustivel tipoCombustivel
    ) {
        super(id, tipo, modelo, fabricante, ano, preco, cor);
        this.quantidadePortas = quantidadePortas;
        this.tipoCombustivel = tipoCombustivel;
    }
}