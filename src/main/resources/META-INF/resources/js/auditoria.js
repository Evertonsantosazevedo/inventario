// Variáveis globais para controle de paginação, dados e ordenação
let logsFull = [];
let currentPage = 1;
const itemsPerPage = 10;
let sortColumn = 'id';
let sortDirection = 'desc'; // Ordena do mais recente para o mais antigo

function listarLogs() {
    fetch("/auth/auditoria/dados")
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else if (response.status === 401 || response.status === 403) {
                alert("Sessão expirada ou acesso negado.");
                window.location.href = "/auth/login";
            } else {
                throw new Error("Erro no servidor");
            }
        })
        .then(lista => {
            if (lista) {
                logsFull = lista;
                ordenarDados(sortColumn, false);
                atualizarTabela();
            }
        })
        .catch(erro => {
            console.error("Erro ao listar logs de auditoria: ", erro);
            alert("Não foi possível carregar o log de auditoria.");
        });
}

window.onload = function () {
    const perfil = sessionStorage.getItem('perfil');
    if (perfil !== 'ADMINISTRADOR') {
        alert("Acesso negado. Apenas administradores podem visualizar a auditoria.");
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

    listarLogs();
}

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

    logsFull.sort((a, b) => {
        let valA = a[coluna];
        let valB = b[coluna];

        // Trata strings para comparação ignorando maiúsculas/minúsculas
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
    const tabela = document.getElementById("tabelaAuditoria");
    tabela.innerHTML = "";

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const logsExibidos = logsFull.slice(startIndex, endIndex);

    logsExibidos.forEach(l => {
        let dataStr = new Date(l.dataHora).toLocaleString('pt-BR');

        let linha = `<tr>
            <td>${l.id}</td>
            <td>${l.nomeUsuario}</td>
            <td>${l.emailUsuario}</td>
            <td><code>${l.acao}</code></td>
            <td>${dataStr}</td>
        </tr>`;

        tabela.innerHTML += linha;
    });

    atualizarControlesPaginacao();
}

function atualizarControlesPaginacao() {
    const totalPages = Math.ceil(logsFull.length / itemsPerPage) || 1;
    document.getElementById("infoPagina").innerText = `Página ${currentPage} de ${totalPages}`;
    document.getElementById("btnAnterior").disabled = (currentPage === 1);
    document.getElementById("btnProximo").disabled = (currentPage === totalPages);
}

function mudarPagina(delta) {
    const totalPages = Math.ceil(logsFull.length / itemsPerPage) || 1;
    const novaPagina = currentPage + delta;

    if (novaPagina >= 1 && novaPagina <= totalPages) {
        currentPage = novaPagina;
        atualizarTabela();
    }
}
