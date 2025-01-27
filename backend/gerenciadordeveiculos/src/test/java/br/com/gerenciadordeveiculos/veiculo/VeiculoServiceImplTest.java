package br.com.gerenciadordeveiculos.veiculo;

import br.com.gerenciadordeveiculos.dtos.VeiculoRequestDTO;
import br.com.gerenciadordeveiculos.dtos.VeiculoResponseDTO;
import br.com.gerenciadordeveiculos.entities.Carro;
import br.com.gerenciadordeveiculos.entities.Moto;
import br.com.gerenciadordeveiculos.enums.TipoCombustivel;
import br.com.gerenciadordeveiculos.enums.TipoVeiculo;
import br.com.gerenciadordeveiculos.exceptions.ExcecaoNegocio;
import br.com.gerenciadordeveiculos.repositories.VeiculoRepository;
import br.com.gerenciadordeveiculos.services.VeiculoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceImplTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private VeiculoServiceImpl veiculoService;

    private VeiculoRequestDTO dtoCarro;
    private VeiculoRequestDTO dtoMoto;

    @BeforeEach
    void setup() {
        dtoCarro = new VeiculoRequestDTO();
        dtoCarro.setTipo(TipoVeiculo.CARRO);
        dtoCarro.setModelo("Gol");
        dtoCarro.setFabricante("VW");
        dtoCarro.setAno(2022);
        dtoCarro.setPreco(50000.0);
        dtoCarro.setCor("Preto");
        dtoCarro.setQuantidadePortas(4);
        dtoCarro.setTipoCombustivel(TipoCombustivel.GASOLINA);

        dtoMoto = new VeiculoRequestDTO();
        dtoMoto.setTipo(TipoVeiculo.MOTO);
        dtoMoto.setModelo("CG 160");
        dtoMoto.setFabricante("Honda");
        dtoMoto.setAno(2021);
        dtoMoto.setPreco(9000.0);
        dtoMoto.setCor("Prata");
        dtoMoto.setCilindrada(160);
    }

    @Test
    void deveCriarCarroComSucesso() {
        VeiculoResponseDTO resp = veiculoService.criarVeiculo(dtoCarro);
        verify(veiculoRepository, times(1)).salvarCarro(any(Carro.class));
        verify(veiculoRepository, never()).salvarMoto(any(Moto.class));
        assertNotNull(resp);
        assertEquals(TipoVeiculo.CARRO, resp.getTipo());
        assertEquals("Gol", resp.getModelo());
        assertEquals("VW", resp.getFabricante());
    }

    @Test
    void deveCriarMotoComSucesso() {
        VeiculoResponseDTO resp = veiculoService.criarVeiculo(dtoMoto);
        verify(veiculoRepository, times(1)).salvarMoto(any(Moto.class));
        verify(veiculoRepository, never()).salvarCarro(any(Carro.class));
        assertNotNull(resp);
        assertEquals(TipoVeiculo.MOTO, resp.getTipo());
        assertEquals("CG 160", resp.getModelo());
        assertEquals("Honda", resp.getFabricante());
    }

    @Test
    void deveLancarExcecaoAoCriarVeiculoComTipoInvalido() {
        VeiculoRequestDTO dtoInvalido = new VeiculoRequestDTO();
        dtoInvalido.setTipo(null);
        dtoInvalido.setModelo("Teste");
        dtoInvalido.setFabricante("Fabricante X");
        dtoInvalido.setAno(2022);
        dtoInvalido.setPreco(1000.0);
        assertThrows(ExcecaoNegocio.class, () -> veiculoService.criarVeiculo(dtoInvalido));
        verifyNoInteractions(veiculoRepository);
    }

    @Test
    void deveBuscarVeiculosSemFiltroComSucesso() {
        Carro c = new Carro(1, TipoVeiculo.CARRO, "Gol", "VW", 2022, 50000.0, "Preto", 4, TipoCombustivel.GASOLINA);
        Moto m = new Moto(2, TipoVeiculo.MOTO, "CG 160", "Honda", 2021, 9000.0, "Prata", 160);
        when(veiculoRepository.buscarTodosPaginado(0, 5)).thenReturn(List.of(c, m));
        List<VeiculoResponseDTO> lista = veiculoService.buscarVeiculosPaginado(0, 5, null, null, null, null);
        verify(veiculoRepository, times(1)).buscarTodosPaginado(0, 5);
        verify(veiculoRepository, never()).buscarPorFiltrosPaginado(any(), any(), any(), any(), anyInt(), anyInt());
        assertEquals(2, lista.size());
    }

    @Test
    void deveBuscarVeiculosComFiltro() {
        Carro c = new Carro(10, TipoVeiculo.CARRO, "Fiesta", "Ford", 2020, 40000.0, "Branco", 4, TipoCombustivel.ETANOL);
        when(veiculoRepository.buscarPorFiltrosPaginado(TipoVeiculo.CARRO, "Fiesta", 2020, "Branco", 0, 5))
                .thenReturn(List.of(c));
        List<VeiculoResponseDTO> lista = veiculoService.buscarVeiculosPaginado(
                0, 5,
                TipoVeiculo.CARRO,
                "Fiesta",
                2020,
                "Branco"
        );
        verify(veiculoRepository, times(1)).buscarPorFiltrosPaginado(TipoVeiculo.CARRO, "Fiesta", 2020, "Branco", 0, 5);
        verify(veiculoRepository, never()).buscarTodosPaginado(anyInt(), anyInt());
        assertEquals(1, lista.size());
        assertEquals("Fiesta", lista.get(0).getModelo());
    }

    @Test
    void deveLancarExcecaoSePaginaNegativa() {
        assertThrows(ExcecaoNegocio.class, () -> veiculoService.buscarVeiculosPaginado(-1, 5, null, null, null, null));
        verifyNoInteractions(veiculoRepository);
    }

    @Test
    void deveLancarExcecaoSeSizeInvalido() {
        assertThrows(ExcecaoNegocio.class, () -> veiculoService.buscarVeiculosPaginado(0, 0, null, null, null, null));
        verifyNoInteractions(veiculoRepository);
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        Carro existente = new Carro(11, TipoVeiculo.CARRO, "Onix", "Chevrolet", 2022, 60000.0, "Azul", 4, TipoCombustivel.FLEX);
        when(veiculoRepository.buscarPorId(11)).thenReturn(existente);
        VeiculoResponseDTO resp = veiculoService.buscarPorId(11);
        verify(veiculoRepository, times(1)).buscarPorId(11);
        assertNotNull(resp);
        assertEquals(11, resp.getId());
        assertEquals("Onix", resp.getModelo());
    }

    @Test
    void deveLancarExcecaoAoBuscarIdInexistente() {
        when(veiculoRepository.buscarPorId(999)).thenReturn(null);
        assertThrows(ExcecaoNegocio.class, () -> veiculoService.buscarPorId(999));
        verify(veiculoRepository, times(1)).buscarPorId(999);
    }

    @Test
    void deveAtualizarCarroComSucesso() {
        Carro existente = new Carro(1, TipoVeiculo.CARRO, "Gol", "VW", 2021, 45000.0, "Preto", 4, TipoCombustivel.GASOLINA);
        when(veiculoRepository.buscarPorId(1)).thenReturn(existente);
        VeiculoResponseDTO resp = veiculoService.atualizarVeiculo(1, dtoCarro);
        verify(veiculoRepository, times(1)).buscarPorId(1);
        verify(veiculoRepository, times(1)).atualizarCarro(any(Carro.class));
        verify(veiculoRepository, never()).atualizarMoto(any(Moto.class));
        assertNotNull(resp);
        assertEquals(TipoVeiculo.CARRO, resp.getTipo());
        assertEquals("Gol", resp.getModelo());
    }

    @Test
    void deveAtualizarMotoComSucesso() {
        Moto existente = new Moto(2, TipoVeiculo.MOTO, "CG 160", "Honda", 2021, 9000.0, "Prata", 160);
        when(veiculoRepository.buscarPorId(2)).thenReturn(existente);
        VeiculoResponseDTO resp = veiculoService.atualizarVeiculo(2, dtoMoto);
        verify(veiculoRepository, times(1)).buscarPorId(2);
        verify(veiculoRepository, times(1)).atualizarMoto(any(Moto.class));
        verify(veiculoRepository, never()).atualizarCarro(any(Carro.class));
        assertNotNull(resp);
        assertEquals("CG 160", resp.getModelo());
        assertEquals(160, resp.getCilindrada());
    }

    @Test
    void deveLancarExcecaoSeVeiculoNaoExistirAoAtualizar() {
        when(veiculoRepository.buscarPorId(999)).thenReturn(null);
        assertThrows(ExcecaoNegocio.class, () -> veiculoService.atualizarVeiculo(999, dtoCarro));
        verify(veiculoRepository, times(1)).buscarPorId(999);
        verify(veiculoRepository, never()).atualizarCarro(any());
        verify(veiculoRepository, never()).atualizarMoto(any());
    }

    @Test
    void deveLancarExcecaoSeTipoInconsistenteNoUpdate() {
        Carro existente = new Carro(1, TipoVeiculo.CARRO, "Gol", "VW", 2021, 45000.0, "Preto", 4, TipoCombustivel.GASOLINA);
        when(veiculoRepository.buscarPorId(1)).thenReturn(existente);
        assertThrows(ExcecaoNegocio.class, () -> veiculoService.atualizarVeiculo(1, dtoMoto));
        verify(veiculoRepository, times(1)).buscarPorId(1);
        verify(veiculoRepository, never()).atualizarCarro(any());
        verify(veiculoRepository, never()).atualizarMoto(any());
    }

    @Test
    void deveDeletarComSucesso() {
        Carro existente = new Carro(10, TipoVeiculo.CARRO, "Fiesta", "Ford", 2020, 40000.0, "Branco", 4, TipoCombustivel.ETANOL);
        when(veiculoRepository.buscarPorId(10)).thenReturn(existente);
        veiculoService.deletarVeiculo(10);
        verify(veiculoRepository, times(1)).buscarPorId(10);
        verify(veiculoRepository, times(1)).deletar(10);
    }

    @Test
    void deveLancarExcecaoAoDeletarVeiculoInexistente() {
        when(veiculoRepository.buscarPorId(999)).thenReturn(null);
        assertThrows(ExcecaoNegocio.class, () -> veiculoService.deletarVeiculo(999));
        verify(veiculoRepository, times(1)).buscarPorId(999);
        verify(veiculoRepository, never()).deletar(999);
    }
}
