// Variáveis globais para controle de paginação, dados e ordenação
let movimentacoesFull = [];
let produtosLista = [];
let currentPage = 1;
const itemsPerPage = 6;
let sortColumn = 'id';
let sortDirection = 'desc'; // Histórico geralmente começa do mais novo

function listarMovimentacoes() {
    fetch("/movimentacoes")
    .then(response => {
        if (response.status === 200) return response.json();
        if (response.status === 401 || response.status === 403) window.location.href = "/auth/login";
    })
    .then(lista => {
        movimentacoesFull = lista;
        ordenarDados(sortColumn, false);
        atualizarTabela();
    })
    .catch(erro => console.error("Erro ao listar movimentações: ", erro));
}

function carregarProdutos() {
    fetch("/produtos")
    .then(response => response.json())
    .then(lista => {
        produtosLista = lista;
        const select = document.getElementById("produtoSelect");
        select.innerHTML = '<option value="">Selecione um produto...</option>';
        
        lista.forEach(p => {
            const option = document.createElement("option");
            option.value = p.id;
            option.text = `${p.nome} (${p.marca})`;
            select.appendChild(option);
        });
    });
}

window.onload = function() {
    const perfil = sessionStorage.getItem('perfil');
    if (!perfil) {
        window.location.href = "/auth/login";
        return;
    }

    // Configura os ouvintes de clique nos cabeçalhos para ordenação
    const headers = document.querySelectorAll("th[data-column]");
    headers.forEach(header => {
        header.addEventListener("click", () => {
            ordenarDados(header.getAttribute("data-column"), true);
            atualizarTabela();
        });
    });

    listarMovimentacoes();
    carregarProdutos();
};

// SVGs para os ícones
const iconEntrada = `<svg viewBox="0 0 24 24" style="width:16px; height:16px; margin-right:5px; vertical-align:middle;"><path d="M4 12l1.41 1.41L11 7.83V20h2V7.83l5.58 5.59L20 12l-8-8-8 8z"/></svg>`;
const iconSaida = `<svg viewBox="0 0 24 24" style="width:16px; height:16px; margin-right:5px; vertical-align:middle;"><path d="M20 12l-1.41-1.41L13 16.17V4h-2v12.17l-5.58-5.59L4 12l8 8 8-8z"/></svg>`;

// Ordenação
function ordenarDados(coluna, alternarDirecao) {
    if (alternarDirecao) {
        if (sortColumn === coluna) {
            sortDirection = sortDirection === 'asc' ? 'desc' : 'asc';
        } else {
            sortColumn = coluna;
            sortDirection = 'asc';
        }
    }

    movimentacoesFull.sort((a, b) => {
        let valA = a[coluna];
        let valB = b[coluna];

        if (typeof valA === 'string') valA = valA.toLowerCase();
        if (typeof valB === 'string') valB = valB.toLowerCase();

        if (valA < valB) return sortDirection === 'asc' ? -1 : 1;
        if (valA > valB) return sortDirection === 'asc' ? 1 : -1;
        return 0;
    });

    atualizarIconesOrdenacao();
}

function atualizarIconesOrdenacao() {
    document.querySelectorAll("th").forEach(th => {
        th.classList.remove("sort-asc", "sort-desc");
        if (th.getAttribute("data-column") === sortColumn) {
            th.classList.add(sortDirection === 'asc' ? "sort-asc" : "sort-desc");
        }
    });
}

// Renderização da tabela
function atualizarTabela() {
    let tabela = document.getElementById("tabelaMovimentacoes");
    tabela.innerHTML = "";

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const movsExibidas = movimentacoesFull.slice(startIndex, endIndex);

    movsExibidas.forEach(m => {
        let dataStr = new Date(m.dataHora).toLocaleString('pt-BR');
        
        let tipoHTML = "";
        if (m.tipoMovimentacao === 'ENTRADA') {
            tipoHTML = `<span style="color: #2ecc71; font-weight: 600; display: flex; align-items: center;">${iconEntrada} ENTRADA</span>`;
        } else {
            tipoHTML = `<span style="color: #e74c3c; font-weight: 600; display: flex; align-items: center;">${iconSaida} SAÍDA</span>`;
        }

        let linha = `<tr>
            <td>${m.id}</td>
            <td>${m.nomeProduto}</td>
            <td>${m.nomeUsuario}</td>
            <td>${m.quantidade}</td>
            <td>${tipoHTML}</td>
            <td>${dataStr}</td>
        </tr>`;
        tabela.innerHTML += linha;
    });

    atualizarControlesPaginacao();
}

function atualizarControlesPaginacao() {
    const totalPages = Math.ceil(movimentacoesFull.length / itemsPerPage) || 1;
    document.getElementById("infoPagina").innerText = `Página ${currentPage} de ${totalPages}`;
    document.getElementById("btnAnterior").disabled = (currentPage === 1);
    document.getElementById("btnProximo").disabled = (currentPage === totalPages);
}

function mudarPagina(delta) {
    const totalPages = Math.ceil(movimentacoesFull.length / itemsPerPage) || 1;
    const novaPagina = currentPage + delta;
    if (novaPagina >= 1 && novaPagina <= totalPages) {
        currentPage = novaPagina;
        atualizarTabela();
    }
}

// Controle de Modais
function abrirModalMovimentacao() {
    // Agora tanto administrador quanto operador podem dar entrada e saída
    document.getElementById("optEntrada").style.display = "block";

    ajustarModalPorTipo();
    document.getElementById("modalMovimentacao").style.display = "flex";
}

function fecharModais() {
    document.getElementById("modalMovimentacao").style.display = "none";
}

function ajustarModalPorTipo() {
    const tipo = document.getElementById("tipoMov").value;
    const btn = document.getElementById("btnConfirmarMov");
    const titulo = document.getElementById("tituloModal");

    if (tipo === 'ENTRADA') {
        titulo.innerText = "Registrar Entrada de Estoque";
        btn.innerText = "Confirmar Entrada";
    } else {
        titulo.innerText = "Registrar Saída de Estoque";
        btn.innerText = "Confirmar Saída";
    }
}

function confirmarMovimentacao() {
    const idProduto = document.getElementById("produtoSelect").value;
    const tipo = document.getElementById("tipoMov").value;
    const qtd = parseInt(document.getElementById("qtdMov").value);

    if (!idProduto) {
        alert("Por favor, selecione um produto.");
        return;
    }
    if (isNaN(qtd) || qtd <= 0) {
        alert("Informe uma quantidade válida.");
        return;
    }

    if (tipo === 'ENTRADA') {
        registrarEntrada(idProduto, qtd);
    } else {
        registrarSaida(idProduto, qtd);
    }
}

function registrarEntrada(id, qtd) {
    fetch(`/produtos/${id}/entrada`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ quantidade: qtd })
    })
    .then(response => {
        if (response.status === 204) {
            alert("Entrada registrada com sucesso!");
            fecharModais();
            listarMovimentacoes();
        } else {
            alert("Erro ao registrar entrada. Verifique suas permissões.");
        }
    });
}

function registrarSaida(id, qtd) {
    fetch(`/movimentacoes/saida?idProduto=${id}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ quantidade: qtd })
    })
    .then(response => {
        if (response.status === 204) {
            alert("Saída registrada com sucesso!");
            fecharModais();
            listarMovimentacoes();
        } else if (response.status === 400 || response.status === 422) {
            alert("Erro: Estoque insuficiente para esta saída.");
        } else {
            alert("Erro ao registrar saída.");
        }
    });
}