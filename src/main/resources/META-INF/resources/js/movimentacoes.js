function listarMovimentacoes() {
    fetch("/movimentacoes")
    .then(response => {
        if (response.status === 200) return response.json();
        if (response.status === 401 || response.status === 403) window.location.href = "/auth/login";
    })
    .then(movs => atualizarTabela(movs));
}

function atualizarTabela(movs) {
    let tabela = document.getElementById("tabelaMovimentacoes");
    tabela.innerHTML = "";

    movs.forEach(m => {
        let dataStr = new Date(m.dataHora).toLocaleString('pt-BR');
        let tipoClass = m.tipoMovimentacao === 'ENTRADA' ? 'status-ativo' : 'status-inativo'; // Reutilizando classes de status para cores
        
        let linha = `<tr>
            <td>${m.id}</td>
            <td>${m.nomeProduto}</td>
            <td>${m.nomeUsuario}</td>
            <td>${m.quantidade}</td>
            <td>${m.tipoMovimentacao}</td>
            <td>${dataStr}</td>
        </tr>`;
        tabela.innerHTML += linha;
    });
}

function registrarSaida() {
    let idProduto = document.getElementById("produtoSaida").value;
    let qtd = parseInt(document.getElementById("qtdSaida").value);

    fetch(`/movimentacoes/saida?idProduto=${idProduto}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ quantidade: qtd })
    })
    .then(response => {
        if (response.status === 204) {
            alert("Saída registrada!");
            listarMovimentacoes();
        } else if (response.status === 400) {
            alert("Erro: Estoque insuficiente ou dados inválidos.");
        }
    });
}

window.onload = function() {
    if (!sessionStorage.getItem('perfil')) {
        window.location.href = "/auth/login";
        return;
    }
    listarMovimentacoes();
};
