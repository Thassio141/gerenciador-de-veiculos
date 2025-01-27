package br.com.gerenciadordeveiculos.services;

import br.com.gerenciadordeveiculos.dtos.VeiculoRequestDTO;
import br.com.gerenciadordeveiculos.dtos.VeiculoResponseDTO;
import br.com.gerenciadordeveiculos.entities.Carro;
import br.com.gerenciadordeveiculos.entities.Moto;
import br.com.gerenciadordeveiculos.entities.Veiculo;
import br.com.gerenciadordeveiculos.enums.TipoVeiculo;
import br.com.gerenciadordeveiculos.exceptions.ExcecaoNegocio;
import br.com.gerenciadordeveiculos.repositories.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeiculoServiceImpl implements VeiculoService {

    private final VeiculoRepository veiculoRepository;

    public VeiculoServiceImpl(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    @Override
    public VeiculoResponseDTO criarVeiculo(VeiculoRequestDTO dto) {
        if (dto.getTipo() == TipoVeiculo.CARRO) {
            validarCamposCarro(dto);

            Carro carro = new Carro(
                    null,
                    dto.getTipo(),
                    dto.getModelo(),
                    dto.getFabricante(),
                    dto.getAno(),
                    dto.getPreco(),
                    dto.getCor(),
                    dto.getQuantidadePortas(),
                    dto.getTipoCombustivel()
            );
            veiculoRepository.salvarCarro(carro);
            return converterParaResponseDTO(carro);

        } else if (dto.getTipo() == TipoVeiculo.MOTO) {
            validarCamposMoto(dto);

            Moto moto = new Moto(
                    null,
                    dto.getTipo(),
                    dto.getModelo(),
                    dto.getFabricante(),
                    dto.getAno(),
                    dto.getPreco(),
                    dto.getCor(),
                    dto.getCilindrada()
            );
            veiculoRepository.salvarMoto(moto);
            return converterParaResponseDTO(moto);

        } else {
            throw new ExcecaoNegocio("Tipo de veículo inválido. Use 'CARRO' ou 'MOTO'.");
        }
    }

    @Override
    public List<VeiculoResponseDTO> buscarTodos() {
        List<Veiculo> veiculos = veiculoRepository.buscarTodos();
        return veiculos.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VeiculoResponseDTO buscarPorId(Integer id) {
        Veiculo veiculo = veiculoRepository.buscarPorId(id);
        if (veiculo == null) {
            throw new ExcecaoNegocio("Veículo inexistente para ID: " + id);
        }
        return converterParaResponseDTO(veiculo);
    }

    @Override
    public List<VeiculoResponseDTO> buscarPorFiltros(TipoVeiculo tipo, String modelo, Integer ano,String cor) {
        List<Veiculo> veiculos = veiculoRepository.buscarPorFiltros(tipo, modelo, ano,cor);
        return veiculos.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VeiculoResponseDTO atualizarVeiculo(Integer id, VeiculoRequestDTO dto) {
        Veiculo existente = veiculoRepository.buscarPorId(id);
        if (existente == null) {
            throw new ExcecaoNegocio("Veículo inexistente para ID: " + id);
        }

        if (dto.getTipo() == TipoVeiculo.CARRO && existente instanceof Carro) {
            validarCamposCarro(dto);

            Carro carro = new Carro();
            carro.setId(id);
            carro.setTipo(TipoVeiculo.CARRO);
            carro.setModelo(dto.getModelo());
            carro.setFabricante(dto.getFabricante());
            carro.setAno(dto.getAno());
            carro.setPreco(dto.getPreco());
            carro.setCor(dto.getCor());
            carro.setQuantidadePortas(dto.getQuantidadePortas());
            carro.setTipoCombustivel(dto.getTipoCombustivel());

            veiculoRepository.atualizarCarro(carro);
            return converterParaResponseDTO(carro);

        } else if (dto.getTipo() == TipoVeiculo.MOTO && existente instanceof Moto) {
            validarCamposMoto(dto);

            Moto moto = new Moto();
            moto.setId(id);
            moto.setTipo(TipoVeiculo.MOTO);
            moto.setModelo(dto.getModelo());
            moto.setFabricante(dto.getFabricante());
            moto.setAno(dto.getAno());
            moto.setPreco(dto.getPreco());
            moto.setCor(dto.getCor());
            moto.setCilindrada(dto.getCilindrada());

            veiculoRepository.atualizarMoto(moto);
            return converterParaResponseDTO(moto);

        } else {
            throw new ExcecaoNegocio("Tipo de veículo inconsistente ou inválido para este ID.");
        }
    }

    @Override
    public void deletarVeiculo(Integer id) {
        Veiculo existente = veiculoRepository.buscarPorId(id);
        if (existente == null) {
            throw new ExcecaoNegocio("Impossível deletar. Veículo inexistente para ID: " + id);
        }
        veiculoRepository.deletar(id);
    }

    private void validarCamposCarro(VeiculoRequestDTO dto) {
        if (dto.getQuantidadePortas() == null) {
            throw new ExcecaoNegocio("Quantidade de portas é obrigatória para veículo do tipo CARRO.");
        }
        if (dto.getTipoCombustivel() == null) {
            throw new ExcecaoNegocio("Tipo de combustível é obrigatório para veículo do tipo CARRO.");
        }
        if (dto.getCilindrada() != null) {
            throw new ExcecaoNegocio("Cilindrada deve ser nula para veículo do tipo CARRO.");
        }
    }

    private void validarCamposMoto(VeiculoRequestDTO dto) {
        if (dto.getCilindrada() == null) {
            throw new ExcecaoNegocio("Cilindrada é obrigatória para veículo do tipo MOTO.");
        }
        if (dto.getQuantidadePortas() != null) {
            throw new ExcecaoNegocio("Quantidade de portas deve ser nula para veículo do tipo MOTO.");
        }
        if (dto.getTipoCombustivel() != null) {
            throw new ExcecaoNegocio("Tipo de combustível deve ser nulo para veículo do tipo MOTO.");
        }
    }

    private VeiculoResponseDTO converterParaResponseDTO(Veiculo veiculo) {
        VeiculoResponseDTO dto = new VeiculoResponseDTO();
        dto.setId(veiculo.getId());
        dto.setTipo(veiculo.getTipo());
        dto.setModelo(veiculo.getModelo());
        dto.setFabricante(veiculo.getFabricante());
        dto.setAno(veiculo.getAno());
        dto.setPreco(veiculo.getPreco());
        dto.setCor(veiculo.getCor());

        if (veiculo instanceof Carro) {
            dto.setTipo(TipoVeiculo.CARRO);
            Carro carro = (Carro) veiculo;
            dto.setQuantidadePortas(carro.getQuantidadePortas());
            dto.setTipoCombustivel(carro.getTipoCombustivel());
        } else if (veiculo instanceof Moto) {
            dto.setTipo(TipoVeiculo.MOTO);
            Moto moto = (Moto) veiculo;
            dto.setCilindrada(moto.getCilindrada());
        }

        return dto;
    }
}
