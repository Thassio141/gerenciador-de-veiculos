package br.com.gerenciadordeveiculos.repositories;

import br.com.gerenciadordeveiculos.entities.Carro;
import br.com.gerenciadordeveiculos.entities.Moto;
import br.com.gerenciadordeveiculos.entities.Veiculo;
import br.com.gerenciadordeveiculos.enums.TipoCombustivel;
import br.com.gerenciadordeveiculos.enums.TipoVeiculo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class VeiculoRepository {

    private final JdbcTemplate jdbcTemplate;

    public VeiculoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void salvarCarro(Carro carro) {
        String sql = "INSERT INTO veiculos (tipo, modelo, fabricante, ano, preco, cor, quantidade_portas, tipo_combustivel) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                TipoVeiculo.CARRO.name(),
                carro.getModelo(),
                carro.getFabricante(),
                carro.getAno(),
                carro.getPreco(),
                carro.getCor(),
                carro.getQuantidadePortas(),
                carro.getTipoCombustivel() != null ? carro.getTipoCombustivel().name() : null
        );
    }

    public void salvarMoto(Moto moto) {
        String sql = "INSERT INTO veiculos (tipo, modelo, fabricante, ano, preco, cor, cilindrada) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                TipoVeiculo.MOTO.name(),
                moto.getModelo(),
                moto.getFabricante(),
                moto.getAno(),
                moto.getPreco(),
                moto.getCor(),
                moto.getCilindrada()
        );
    }

    public List<Veiculo> buscarTodos() {
        String sql = "SELECT * FROM veiculos";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapResultSetToVeiculo(rs));
    }

    public Veiculo buscarPorId(Integer id) {
        String sql = "SELECT * FROM veiculos WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapResultSetToVeiculo(rs), id);
    }

    public List<Veiculo> buscarPorFiltros(TipoVeiculo tipo, String modelo, Integer ano, String cor) {
        StringBuilder sb = new StringBuilder("SELECT * FROM veiculos WHERE 1=1");

        if (tipo != null) {
            sb.append(" AND tipo = '").append(tipo.name()).append("'");
        }
        if (modelo != null && !modelo.isEmpty()) {
            sb.append(" AND modelo ILIKE '%").append(modelo).append("%'");
        }
        if (ano != null) {
            sb.append(" AND ano = ").append(ano);
        }
        if (cor != null && !cor.isEmpty()) {
            sb.append(" AND cor ILIKE '%").append(cor).append("%'");
        }

        return jdbcTemplate.query(sb.toString(), (rs, rowNum) -> mapResultSetToVeiculo(rs));
    }

    public void atualizarCarro(Carro carro) {
        String sql = "UPDATE veiculos SET "
                + "modelo = ?, fabricante = ?, ano = ?, preco = ?, cor = ?, "
                + "quantidade_portas = ?, tipo_combustivel = ? "
                + "WHERE id = ?";
        jdbcTemplate.update(sql,
                carro.getModelo(),
                carro.getFabricante(),
                carro.getAno(),
                carro.getPreco(),
                carro.getCor(),
                carro.getQuantidadePortas(),
                carro.getTipoCombustivel() != null ? carro.getTipoCombustivel().name() : null,
                carro.getId()
        );
    }

    public void atualizarMoto(Moto moto) {
        String sql = "UPDATE veiculos SET "
                + "modelo = ?, fabricante = ?, ano = ?, preco = ?, cor = ?, cilindrada = ? "
                + "WHERE id = ?";
        jdbcTemplate.update(sql,
                moto.getModelo(),
                moto.getFabricante(),
                moto.getAno(),
                moto.getPreco(),
                moto.getCor(),
                moto.getCilindrada(),
                moto.getId()
        );
    }

    public void deletar(Integer id) {
        String sql = "DELETE FROM veiculos WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private Veiculo mapResultSetToVeiculo(ResultSet rs) throws SQLException {
        String tipoStr = rs.getString("tipo");
        TipoVeiculo tipo = TipoVeiculo.valueOf(tipoStr.toUpperCase());

        if (tipo == TipoVeiculo.CARRO) {
            Carro carro = new Carro();
            carro.setId(rs.getInt("id"));
            carro.setTipo(TipoVeiculo.CARRO);
            carro.setModelo(rs.getString("modelo"));
            carro.setFabricante(rs.getString("fabricante"));
            carro.setAno(rs.getInt("ano"));
            carro.setPreco(rs.getDouble("preco"));
            carro.setCor(rs.getString("cor"));
            carro.setQuantidadePortas(rs.getInt("quantidade_portas"));

            String combustivelStr = rs.getString("tipo_combustivel");

            if (combustivelStr != null) {
                carro.setTipoCombustivel(TipoCombustivel.valueOf(combustivelStr.toUpperCase()));
            }
            return carro;

        } else {
            Moto moto = new Moto();
            moto.setId(rs.getInt("id"));
            moto.setTipo(TipoVeiculo.MOTO);
            moto.setModelo(rs.getString("modelo"));
            moto.setFabricante(rs.getString("fabricante"));
            moto.setAno(rs.getInt("ano"));
            moto.setPreco(rs.getDouble("preco"));
            moto.setCor(rs.getString("cor"));
            moto.setCilindrada(rs.getInt("cilindrada"));
            return moto;
        }
    }
}
