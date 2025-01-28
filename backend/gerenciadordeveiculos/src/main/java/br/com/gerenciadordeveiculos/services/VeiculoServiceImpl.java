package br.com.gerenciadordeveiculos.services;

import br.com.gerenciadordeveiculos.dtos.VeiculoRequestDTO;
import br.com.gerenciadordeveiculos.dtos.VeiculoResponseDTO;
import br.com.gerenciadordeveiculos.models.Carro;
import br.com.gerenciadordeveiculos.models.Moto;
import br.com.gerenciadordeveiculos.models.Veiculo;
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

            Carro carro = construirCarro(dto, null);
            veiculoRepository.salvarCarro(carro);
            return converterParaResponseDTO(carro);

        } else if (dto.getTipo() == TipoVeiculo.MOTO) {

            Moto moto = construirMoto(dto, null);
            veiculoRepository.salvarMoto(moto);
            return converterParaResponseDTO(moto);

        } else {
            throw new ExcecaoNegocio("Tipo de veículo inválido. Use 'CARRO' ou 'MOTO'.");
        }
    }

    @Override
    public List<VeiculoResponseDTO> buscarVeiculosPaginado(
            Integer page,
            Integer size,
            TipoVeiculo tipo,
            String modelo,
            Integer ano,
            String cor
    ) {
        if (page < 0) {
            throw new ExcecaoNegocio("O número da página não pode ser negativo.");
        }
        if (size <= 0) {
            throw new ExcecaoNegocio("O tamanho da página deve ser maior que zero.");
        }

        Integer offset = page * size;

        boolean temFiltro = (tipo != null || (modelo != null && !modelo.isEmpty())
                || ano != null || (cor != null && !cor.isEmpty()));

        List<Veiculo> veiculos;

        if (temFiltro) {
            veiculos = veiculoRepository.buscarPorFiltrosPaginado(tipo, modelo, ano, cor, offset, size);
        } else {
            veiculos = veiculoRepository.buscarTodosPaginado(offset, size);
        }

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
    public VeiculoResponseDTO atualizarVeiculo(Integer id, VeiculoRequestDTO dto) {
        Veiculo existente = veiculoRepository.buscarPorId(id);
        if (existente == null) {
            throw new ExcecaoNegocio("Veículo inexistente para ID: " + id);
        }

        if (dto.getTipo() == TipoVeiculo.CARRO && existente instanceof Carro) {
            validarCampos(dto);

            Carro carro = construirCarro(dto,id);

            veiculoRepository.atualizarCarro(carro);

            return converterParaResponseDTO(carro);

        } else if (dto.getTipo() == TipoVeiculo.MOTO && existente instanceof Moto) {
            validarCampos(dto);

            Moto moto = construirMoto(dto, id);
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

    private void validarCampos(VeiculoRequestDTO dto) {
        if (dto.getTipo() == TipoVeiculo.CARRO) {
            if (dto.getQuantidadePortas() == null) {
                throw new ExcecaoNegocio("Quantidade de portas é obrigatória para veículo do tipo CARRO.");
            }
            if (dto.getTipoCombustivel() == null) {
                throw new ExcecaoNegocio("Tipo de combustível é obrigatório para veículo do tipo CARRO.");
            }
            if (dto.getCilindrada() != null) {
                throw new ExcecaoNegocio("Cilindrada deve ser nula para veículo do tipo CARRO.");
            }
        } else if (dto.getTipo() == TipoVeiculo.MOTO) {
            if (dto.getCilindrada() == null) {
                throw new ExcecaoNegocio("Cilindrada é obrigatória para veículo do tipo MOTO.");
            }
            if (dto.getQuantidadePortas() != null) {
                throw new ExcecaoNegocio("Quantidade de portas deve ser nula para veículo do tipo MOTO.");
            }
            if (dto.getTipoCombustivel() != null) {
                throw new ExcecaoNegocio("Tipo de combustível deve ser nulo para veículo do tipo MOTO.");
            }
        } else {
            throw new ExcecaoNegocio("Tipo de veículo inválido.");
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

        if (veiculo instanceof Carro carro) {
            dto.setTipo(TipoVeiculo.CARRO);
            dto.setQuantidadePortas(carro.getQuantidadePortas());
            dto.setTipoCombustivel(carro.getTipoCombustivel());
        } else if (veiculo instanceof Moto moto) {
            dto.setTipo(TipoVeiculo.MOTO);
            dto.setCilindrada(moto.getCilindrada());
        }

        return dto;
    }

    private Carro construirCarro(VeiculoRequestDTO dto, Integer id) {
        return new Carro(
                id,
                TipoVeiculo.CARRO,
                dto.getModelo(),
                dto.getFabricante(),
                dto.getAno(),
                dto.getPreco(),
                dto.getCor(),
                dto.getQuantidadePortas(),
                dto.getTipoCombustivel()
        );
    }

    private Moto construirMoto(VeiculoRequestDTO dto, Integer id) {
        return new Moto(
                id,
                TipoVeiculo.MOTO,
                dto.getModelo(),
                dto.getFabricante(),
                dto.getAno(),
                dto.getPreco(),
                dto.getCor(),
                dto.getCilindrada()
        );
    }
}
