function listarProdutos() {
    fetch("/produtos")
    .then(response => {
        if (response.status === 200) return response.json();
        if (response.status === 401 || response.status === 403) window.location.href = "/auth/login";
    })
    .then(produtos => atualizarTabela(produtos));
}

function atualizarTabela(produtos) {
    let tabela = document.getElementById("tabelaProdutos");
    tabela.innerHTML = "";

    produtos.forEach(p => {
        let linha = `<tr>
            <td>${p.id}</td>
            <td>${p.codigo}</td>
            <td>${p.nome}</td>
            <td>${p.marca}</td>
            <td>${p.quantidade}</td>
            <td>R$ ${p.valorVenda.toFixed(2)}</td>
            <td>
                <button onclick="abrirEntrada(${p.id}, '${p.nome}')">Entrada</button>
                <button onclick="deletarProduto(${p.id})" style="color: #e74c3c; border-color: #e74c3c;">Excluir</button>
            </td>
        </tr>`;
        tabela.innerHTML += linha;
    });
}

function salvarProduto() {
    let produto = {
        codigo: document.getElementById("codigoNovo").value,
        nome: document.getElementById("nomeNovo").value,
        marca: document.getElementById("marcaNova").value,
        quantidade: parseInt(document.getElementById("quantidadeInicial").value),
        valorVenda: parseFloat(document.getElementById("valorVendaNovo").value)
    };

    fetch("/produtos", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(produto)
    })
    .then(response => {
        if (response.status === 201) {
            alert("Produto cadastrado!");
            listarProdutos();
        } else {
            alert("Erro ao cadastrar.");
        }
    });
}

function deletarProduto(id) {
    if (!confirm("Confirmar exclusão?")) return;
    fetch(`/produtos/${id}`, { method: "DELETE" })
    .then(response => {
        if (response.status === 204) {
            listarProdutos();
        } else if (response.status === 409) {
            alert("Produto possui movimentações e não pode ser excluído.");
        }
    });
}

function abrirEntrada(id, nome) {
    document.getElementById("idEntrada").value = id;
    document.getElementById("nomeEntrada").innerText = nome;
    document.getElementById("formEntrada").style.display = "block";
}

function fecharEntrada() {
    document.getElementById("formEntrada").style.display = "none";
}

function confirmarEntrada() {
    let id = document.getElementById("idEntrada").value;
    let qtd = parseInt(document.getElementById("qtdEntrada").value);

    fetch(`/produtos/${id}/entrada`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ quantidade: qtd })
    })
    .then(response => {
        if (response.status === 204) {
            fecharEntrada();
            listarProdutos();
        }
    });
}

window.onload = function() {
    if (!sessionStorage.getItem('perfil')) {
        window.location.href = "/auth/login";
        return;
    }
    listarProdutos();
};
