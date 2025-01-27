const API_URL = "http://localhost:8080/api/veiculos";
let modalVeiculo;
let currentPage = 0;
let pageSize = 10;

window.addEventListener("load", () => {
  modalVeiculo = new bootstrap.Modal(document.getElementById("modalVeiculo"));
  listarTodos();
  document.getElementById("veiculoTipo").addEventListener("change", atualizarCamposEspecificos);
  atualizarCamposEspecificos();
});

async function listarTodos() {
  await buscarVeiculos(currentPage, pageSize);
}

async function filtrarVeiculos() {
  const tipo = document.getElementById("filtroTipo").value;
  const cor = document.getElementById("filtroCor").value;
  const modelo = document.getElementById("filtroModelo").value;
  const ano = document.getElementById("filtroAno").value;

  await buscarVeiculos(currentPage, pageSize, { tipo, cor, modelo, ano });
}

async function buscarVeiculos(page, size, filters = {}) {
  const { tipo, cor, modelo, ano } = filters;

  let url = `${API_URL}?page=${page}&size=${size}`;
  if (tipo) url += `&tipo=${tipo}`;
  if (cor) url += `&cor=${cor}`;
  if (modelo) url += `&modelo=${modelo}`;
  if (ano) url += `&ano=${ano}`;

  try {
    const resp = await fetch(url);
    const veiculos = await resp.json();
    preencherTabela(veiculos);
    atualizarControlesPaginacao();
  } catch (err) {
    console.error(err);
  }
}

function preencherTabela(veiculos) {
  const tbody = document.querySelector("#tabelaVeiculos tbody");
  tbody.innerHTML = "";
  veiculos.forEach((v) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${v.id}</td>
      <td>${v.tipo}</td>
      <td>${v.modelo}</td>
      <td>${v.fabricante}</td>
      <td>${v.ano}</td>
      <td>${v.cor}</td>
      <td>${v.preco}</td>
      <td>
        <button class="btn btn-sm btn-info me-1" onclick="detalharVeiculo(${v.id})">
          <i class="bi bi-eye"></i>
        </button>
        <button class="btn btn-sm btn-primary me-1" onclick="editarVeiculo(${v.id})">
          <i class="bi bi-pencil-square"></i>
        </button>
        <button class="btn btn-sm btn-danger" onclick="excluirVeiculo(${v.id})">
          <i class="bi bi-trash"></i>
        </button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

function atualizarControlesPaginacao() {
  const controles = document.querySelector("#controlesPaginacao");
  controles.innerHTML = `
    <button class="btn btn-secondary" ${currentPage === 0 ? "disabled" : ""} onclick="mudarPagina(-1)">
      Anterior
    </button>
    <span class="mx-2">Página ${currentPage + 1}</span>
    <button class="btn btn-secondary" onclick="mudarPagina(1)">Próxima</button>
  `;
}

function mudarPagina(delta) {
  currentPage += delta;
  listarTodos();
}

function abrirModalParaNovo() {
  limparFormularioModal();
  document.getElementById("tituloModalVeiculo").innerText = "Cadastrar Novo Veículo";
  habilitarCamposModal(true);
  document.getElementById("btnSalvarModal").style.display = "inline-block";
  modalVeiculo.show();
}

async function detalharVeiculo(id) {
  try {
    const resp = await fetch(`${API_URL}/${id}`);
    if (!resp.ok) return;
    const v = await resp.json();
    preencherFormularioModal(v);
    document.getElementById("tituloModalVeiculo").innerText = "Detalhes do Veículo";
    habilitarCamposModal(false);
    document.getElementById("btnSalvarModal").style.display = "none";
    modalVeiculo.show();
  } catch (err) {
    console.error(err);
  }
}

async function editarVeiculo(id) {
  try {
    const resp = await fetch(`${API_URL}/${id}`);
    if (!resp.ok) return;
    const v = await resp.json();
    limparFormularioModal();
    preencherFormularioModal(v);
    document.getElementById("tituloModalVeiculo").innerText = "Editar Veículo";
    habilitarCamposModal(true);
    document.getElementById("btnSalvarModal").style.display = "inline-block";
    modalVeiculo.show();
  } catch (err) {
    console.error(err);
  }
}

async function excluirVeiculo(id) {
  if (!confirm(`Deseja realmente excluir o veículo ID ${id}?`)) return;
  try {
    const resp = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    if (resp.status === 204) {
      alert("Veículo excluído com sucesso!");
      listarTodos();
    }
  } catch (err) {
    console.error(err);
  }
}

async function salvarVeiculo() {
  if (!validarCamposObrigatorios()) {
    alert("Preencha todos os campos obrigatórios!");
    return;
  }

  const dto = montarDTOdoModal();
  const id = document.getElementById("veiculoId").value;

  try {
    let url = API_URL;
    let method = "POST";
    if (id) {
      url += `/${id}`;
      method = "PUT";
    }

    const resp = await fetch(url, {
      method: method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dto),
    });

    if (resp.ok) {
      alert("Operação realizada com sucesso!");
      modalVeiculo.hide();
      listarTodos();
    }
  } catch (err) {
    console.error(err);
  }
}

function validarCamposObrigatorios() {
  const tipo = document.getElementById("veiculoTipo").value;
  const modelo = document.getElementById("veiculoModelo").value;
  const fabricante = document.getElementById("veiculoFabricante").value;
  const ano = document.getElementById("veiculoAno").value;
  const preco = document.getElementById("veiculoPreco").value;

  if (!tipo || !modelo || !fabricante || !ano || !preco) {
    return false;
  }

  if (tipo === "CARRO") {
    const portas = document.getElementById("veiculoPortas").value;
    const combustivel = document.getElementById("veiculoCombustivel").value;
    if (!portas || !combustivel) {
      return false;
    }
  } else if (tipo === "MOTO") {
    const cilindrada = document.getElementById("veiculoCilindrada").value;
    if (!cilindrada) {
      return false;
    }
  }

  return true;
}

function montarDTOdoModal() {
  const tipo = document.getElementById("veiculoTipo").value;
  const cor = document.getElementById("veiculoCor").value;
  const modelo = document.getElementById("veiculoModelo").value;
  const fabricante = document.getElementById("veiculoFabricante").value;
  const ano = document.getElementById("veiculoAno").value;
  const preco = document.getElementById("veiculoPreco").value;
  const portas = document.getElementById("veiculoPortas").value;
  const combustivel = document.getElementById("veiculoCombustivel").value;
  const cilindrada = document.getElementById("veiculoCilindrada").value;

  const dto = {
    tipo: tipo,
    cor: cor,
    modelo: modelo,
    fabricante: fabricante,
    ano: ano ? parseInt(ano) : null,
    preco: preco ? parseFloat(preco) : null,
  };

  if (tipo === "CARRO") {
    dto.quantidadePortas = portas ? parseInt(portas) : null;
    dto.tipoCombustivel = combustivel;
  } else {
    dto.cilindrada = cilindrada ? parseInt(cilindrada) : null;
  }

  return dto;
}

function preencherFormularioModal(v) {
  document.getElementById("veiculoId").value = v.id || "";
  document.getElementById("veiculoTipo").value = v.tipo || "CARRO";
  document.getElementById("veiculoCor").value = v.cor || "";
  document.getElementById("veiculoModelo").value = v.modelo || "";
  document.getElementById("veiculoFabricante").value = v.fabricante || "";
  document.getElementById("veiculoAno").value = v.ano || "";
  document.getElementById("veiculoPreco").value = v.preco || "";
  document.getElementById("veiculoPortas").value = v.quantidadePortas ?? "";
  document.getElementById("veiculoCombustivel").value = v.tipoCombustivel ?? "GASOLINA";
  document.getElementById("veiculoCilindrada").value = v.cilindrada ?? "";
  atualizarCamposEspecificos();
}

function limparFormularioModal() {
  document.getElementById("formVeiculo").reset();
  document.getElementById("veiculoId").value = "";
  atualizarCamposEspecificos();
}

function atualizarCamposEspecificos() {
  const tipo = document.getElementById("veiculoTipo").value;
  const camposCarro = document.getElementById("camposCarro");
  const camposMoto = document.getElementById("camposMoto");

  if (tipo === "CARRO") {
    camposCarro.style.display = "flex";
    camposMoto.style.display = "none";
  } else {
    camposCarro.style.display = "none";
    camposMoto.style.display = "flex";
  }
}

function habilitarCamposModal(habilitar) {
  const formEls = document.querySelectorAll("#formVeiculo input, #formVeiculo select");
  formEls.forEach((el) => {
    el.disabled = !habilitar;
  });

  document.getElementById("btnSalvarModal").disabled = !habilitar;
}
