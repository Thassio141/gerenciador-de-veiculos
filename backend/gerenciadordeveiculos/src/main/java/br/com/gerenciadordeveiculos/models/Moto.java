package br.com.gerenciadordeveiculos.models;

import br.com.gerenciadordeveiculos.enums.TipoVeiculo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Moto extends Veiculo {

    private Integer cilindrada;

    public Moto(
            Integer id,
            TipoVeiculo tipo,
            String modelo,
            String fabricante,
            Integer ano,
            Double preco,
            String cor,
            Integer cilindrada
    ) {
        super(id, tipo, modelo, fabricante, ano, preco, cor);
        this.cilindrada = cilindrada;
    }

}