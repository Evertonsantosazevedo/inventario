// Variáveis globais para controle de paginação, dados e ordenação
let produtosFull = [];
let currentPage = 1;
const itemsPerPage = 6;
let sortColumn = 'id';
let sortDirection = 'asc';

// SVGs para os ícones
const iconEditar = `<svg viewBox="0 0 24 24"><path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/></svg>`;
const iconEntrada = `<svg viewBox="0 0 24 24"><path d="M11 9h2V6h3V4h-3V1h-2v3H8v2h3v3zm-4 9c-1.1 0-1.99.9-1.99 2S5.9 22 7 22s2-.9 2-2-.9-2-2-2zm10 0c-1.1 0-1.99.9-1.99 2s.89 2 1.99 2 2-.9 2-2-.9-2-2-2zm-8.9-5h7.45c.75 0 1.41-.41 1.75-1.03l3.58-6.49c.37-.66-.11-1.48-.87-1.48H5.21l-.94-2H1v2h2l3.6 7.59-1.35 2.44C4.52 15.37 5.48 17 7 17h12v-2H7l1.1-2z"/></svg>`;
const iconExcluir = `<svg viewBox="0 0 24 24"><path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/></svg>`;

function listarProdutos() {
    console.log("Iniciando busca de produtos...");
    fetch("/produtos")
    .then(response => {
        console.log("Resposta recebida:", response.status);
        if (response.status === 200) return response.json();
        if (response.status === 401 || response.status === 403) {
            alert("Sessão expirada ou acesso negado.");
            window.location.href = "/auth/login";
            return;
        }
        throw new Error("Erro ao buscar produtos: " + response.status);
    })
    .then(lista => {
        if (lista) {
            console.log("Produtos carregados:", lista.length);
            produtosFull = lista;
            ordenarDados(sortColumn, false);
            atualizarTabela();
        }
    })
    .catch(erro => {
        console.error("Erro ao listar:", erro);
        alert("Não foi possível carregar a lista de produtos.");
    });
}

window.onload = function() {
    const perfil = sessionStorage.getItem('perfil');
    if (!perfil) {
        window.location.href = "/auth/login";
        return;
    }

    // Esconde o botão de Novo Produto se não for administrador
    if (perfil !== 'ADMINISTRADOR') {
        const btnNovo = document.querySelector(".btn-novo");
        if (btnNovo) btnNovo.style.display = "none";
    }

    // Configura os ouvintes de clique nos cabeçalhos para ordenação
    const headers = document.querySelectorAll("th[data-column]");
    headers.forEach(header => {
        header.addEventListener("click", () => {
            ordenarDados(header.getAttribute("data-column"), true);
            atualizarTabela();
        });
    });

    listarProdutos();
};

// Funções de formatação
function formatarMoeda(valor) {
    if (valor === null || valor === undefined) return "R$ 0,00";
    return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

// Ordenação
function ordenarDados(coluna, alternarDirecao) {
    if (!produtosFull || !Array.isArray(produtosFull)) return;

    if (alternarDirecao) {
        if (sortColumn === coluna) {
            sortDirection = sortDirection === 'asc' ? 'desc' : 'asc';
        } else {
            sortColumn = coluna;
            sortDirection = 'asc';
        }
    }

    produtosFull.sort((a, b) => {
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
    const tabela = document.getElementById("tabelaProdutos");
    if (!tabela) return;
    
    tabela.innerHTML = "";
    const perfil = sessionStorage.getItem('perfil');

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const produtosExibidos = produtosFull.slice(startIndex, endIndex);

    if (produtosExibidos.length === 0 && currentPage > 1) {
        currentPage--;
        atualizarTabela();
        return;
    }

    produtosExibidos.forEach(p => {
        let acoesHTML = "";
        
        // Operador e Administrador podem dar entrada
        acoesHTML += `<button class="btn-acao btn-ativar" title="Entrada de Estoque" onclick="abrirModalEntrada(${p.id}, '${p.nome}')">${iconEntrada}</button>`;
        
        // Apenas Administrador pode editar e excluir
        if (perfil === 'ADMINISTRADOR') {
            acoesHTML = `
                <button class="btn-acao btn-editar" title="Editar" onclick="abrirModalEdicao(${p.id})">${iconEditar}</button>
                ` + acoesHTML + `
                <button class="btn-acao btn-desativar" title="Excluir" onclick="deletarProduto(${p.id})">${iconExcluir}</button>
            `;
        }

        let linha = `<tr>
            <td>${p.id}</td>
            <td>${p.codigo}</td>
            <td>${p.nome}</td>
            <td>${p.marca}</td>
            <td>${p.quantidade}</td>
            <td>${formatarMoeda(p.valorVenda)}</td>
            <td><div style="display: flex; gap: 5px;">${acoesHTML}</div></td>
        </tr>`;
        tabela.innerHTML += linha;
    });

    atualizarControlesPaginacao();
}

function atualizarControlesPaginacao() {
    const totalPages = Math.ceil(produtosFull.length / itemsPerPage) || 1;
    const infoPagina = document.getElementById("infoPagina");
    const btnAnterior = document.getElementById("btnAnterior");
    const btnProximo = document.getElementById("btnProximo");

    if (infoPagina) infoPagina.innerText = `Página ${currentPage} de ${totalPages}`;
    if (btnAnterior) btnAnterior.disabled = (currentPage === 1);
    if (btnProximo) btnProximo.disabled = (currentPage === totalPages);
}

function mudarPagina(delta) {
    const totalPages = Math.ceil(produtosFull.length / itemsPerPage) || 1;
    const novaPagina = currentPage + delta;
    if (novaPagina >= 1 && novaPagina <= totalPages) {
        currentPage = novaPagina;
        atualizarTabela();
    }
}

// Controle de Modais
function abrirModalCadastro() {
    document.getElementById("modalCadastro").style.display = "flex";
}

function abrirModalEdicao(id) {
    const produto = produtosFull.find(p => p.id === id);
    if (produto) {
        document.getElementById("idEdit").value = produto.id;
        document.getElementById("codigoEdit").value = produto.codigo;
        document.getElementById("nomeEdit").value = produto.nome;
        document.getElementById("marcaEdit").value = produto.marca;
        document.getElementById("valorVendaEdit").value = produto.valorVenda;
        document.getElementById("modalEdicao").style.display = "flex";
    }
}

function abrirModalEntrada(id, nome) {
    document.getElementById("idEntrada").value = id;
    document.getElementById("nomeEntrada").innerText = nome;
    document.getElementById("qtdEntrada").value = 1;
    document.getElementById("modalEntrada").style.display = "flex";
}

function fecharModais() {
    document.getElementById("modalCadastro").style.display = "none";
    document.getElementById("modalEdicao").style.display = "none";
    document.getElementById("modalEntrada").style.display = "none";
}

// Ações de Persistência
function salvarProduto() {
    let produto = {
        codigo: document.getElementById("codigoNovo").value,
        nome: document.getElementById("nomeNovo").value,
        marca: document.getElementById("marcaNova").value,
        quantidade: parseInt(document.getElementById("quantidadeInicial").value),
        valorVenda: parseFloat(document.getElementById("valorVendaNovo").value)
    };

    if (!produto.codigo || !produto.nome || isNaN(produto.valorVenda)) {
        alert("Por favor, preencha os campos obrigatórios.");
        return;
    }

    fetch("/produtos", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(produto)
    })
    .then(response => {
        if (response.status === 201) {
            alert("Produto cadastrado com sucesso!");
            fecharModais();
            listarProdutos();
        } else {
            alert("Erro ao cadastrar.");
        }
    })
    .catch(erro => console.error("Erro ao cadastrar:", erro));
}

function enviarEdicao() {
    const id = document.getElementById("idEdit").value;
    const produto = {
        codigo: document.getElementById("codigoEdit").value,
        nome: document.getElementById("nomeEdit").value,
        marca: document.getElementById("marcaEdit").value,
        valorVenda: parseFloat(document.getElementById("valorVendaEdit").value)
    };

    fetch(`/produtos/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(produto)
    })
    .then(response => {
        if (response.status === 204) {
            alert("Produto atualizado com sucesso!");
            fecharModais();
            listarProdutos();
        } else {
            alert("Erro ao atualizar.");
        }
    })
    .catch(erro => console.error("Erro ao atualizar:", erro));
}

function confirmarEntrada() {
    let id = document.getElementById("idEntrada").value;
    let qtd = parseInt(document.getElementById("qtdEntrada").value);

    if (isNaN(qtd) || qtd <= 0) {
        alert("Informe uma quantidade válida.");
        return;
    }

    fetch(`/produtos/${id}/entrada`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ quantidade: qtd })
    })
    .then(response => {
        if (response.status === 204) {
            alert("Entrada registrada com sucesso!");
            fecharModais();
            listarProdutos();
        }
    })
    .catch(erro => console.error("Erro na entrada:", erro));
}

function deletarProduto(id) {
    if (!confirm("Confirmar exclusão deste produto?")) return;
    fetch(`/produtos/${id}`, { method: "DELETE" })
    .then(response => {
        if (response.status === 204) {
            listarProdutos();
        } else if (response.status === 409) {
            alert("Erro: O produto possui movimentações registradas e não pode ser excluído.");
        }
    })
    .catch(erro => console.error("Erro ao deletar:", erro));
}