package br.com.gerenciadordeveiculos.entities;

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

    public Moto(Integer id, String modelo, String fabricante, Integer ano, Double preco, Integer cilindrada) {
        super(id, modelo, fabricante, ano, preco);
        this.cilindrada = cilindrada;
    }

}