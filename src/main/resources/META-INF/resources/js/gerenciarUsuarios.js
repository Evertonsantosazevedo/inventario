// Variáveis globais para controle de paginação, dados e ordenação
let usuariosFull = [];
let currentPage = 1;
const itemsPerPage = 6;
let sortColumn = 'id';
let sortDirection = 'asc';

function listaUsuarios() {
    let url = "http://localhost:8080/usuarios";

    fetch(url, {
            method: "GET",
            headers: { "Content-Type": "application/json" }
        }
    )
        .then(function (response) {
            if (response.status === 200) {
                return response.json()
            } else if (response.status === 401 || response.status === 403) {
                alert("Sessão expirada ou acesso negado.")
                window.location.href = "/auth/login"
            } else {
                throw new Error("Erro no servidor")
            }
        })
        .then(function (lista) {
            usuariosFull = lista;
            ordenarDados(sortColumn, false); // Ordena mantendo a direção atual
            atualizarTabela();
        })
        .catch(function (erro) {
            console.error("Erro ao listar: ", erro)
        })
}

window.onload = function () {
    const perfil = sessionStorage.getItem('perfil');
    if (perfil == null) {
        alert("Acesso negado");
        window.location.href = "/auth/login"
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

    listaUsuarios()
}

// Funções de formatação e renderização
function renderizarStatus(ativo) {
    const dotClass = ativo ? 'dot-ativo' : 'dot-inativo';
    const texto = ativo ? 'Ativo' : 'Inativo';
    return `
        <div class="status-container">
            <span class="status-dot ${dotClass}"></span>
            <span>${texto}</span>
        </div>
    `;
}

function formatarPerfil(perfil) {
    if (perfil == null) return ""
    return perfil.charAt(0).toUpperCase() + perfil.slice(1).toLowerCase()
}

// SVGs para os ícones
const iconEditar = `<svg viewBox="0 0 24 24"><path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/></svg>`;
const iconDesativar = `<svg viewBox="0 0 24 24"><path d="M13 3h-2v10h2V3zm4.83 2.17l-1.42 1.42C17.99 7.86 19 9.81 19 12c0 3.87-3.13 7-7 7s-7-3.13-7-7c0-2.19 1.01-4.14 2.58-5.42L6.17 5.17C4.23 6.82 3 9.26 3 12c0 4.97 4.03 9 9 9s9-4.03 9-9c0-2.74-1.23-5.18-3.17-6.83z"/></svg>`;
const iconAtivar = `<svg viewBox="0 0 24 24"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/></svg>`;

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

    usuariosFull.sort((a, b) => {
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
    let tabela = document.getElementById("tabelaUsuarios")
    tabela.innerHTML = "";

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const usuariosExibidos = usuariosFull.slice(startIndex, endIndex);

    for (let i = 0; i < usuariosExibidos.length; i++) {
        let u = usuariosExibidos[i]

        let acaoBtn = u.ativo 
            ? `<button class="btn-acao btn-desativar" title="Desativar" onclick="desativarUsuario(${u.id})">${iconDesativar}</button>`
            : `<button class="btn-acao btn-ativar" title="Ativar" onclick="ativarUsuario(${u.id})">${iconAtivar}</button>`;

        let linha = "<tr>" +
            "<td>" + u.id + "</td>" +
            "<td>" + u.nome + "</td>" +
            "<td>" + u.email + "</td>" +
            "<td>" + formatarPerfil(u.perfil) + "</td>" +
            "<td>" + renderizarStatus(u.ativo) + "</td>" +
            "<td>" +
            `<button class="btn-acao btn-editar" title="Editar" onclick="editarUsuario(${u.id})">${iconEditar}</button>` +
            acaoBtn +
            "</td>" +
            "</tr>"

        tabela.innerHTML += linha;
    }

    atualizarControlesPaginacao();
}

function atualizarControlesPaginacao() {
    const totalPages = Math.ceil(usuariosFull.length / itemsPerPage) || 1;
    document.getElementById("infoPagina").innerText = `Página ${currentPage} de ${totalPages}`;
    document.getElementById("btnAnterior").disabled = (currentPage === 1);
    document.getElementById("btnProximo").disabled = (currentPage === totalPages);
}

function mudarPagina(delta) {
    const totalPages = Math.ceil(usuariosFull.length / itemsPerPage) || 1;
    const novaPagina = currentPage + delta;

    if (novaPagina >= 1 && novaPagina <= totalPages) {
        currentPage = novaPagina;
        atualizarTabela();
    }
}

// Modais
function abrirModalCadastro() {
    document.getElementById("modalCadastro").style.display = "flex";
}

function abrirModalEdicao(id) {
    const usuario = usuariosFull.find(u => u.id === id);
    if (usuario) {
        document.getElementById("idEdite").value = usuario.id;
        document.getElementById("nomeEdit").value = usuario.nome;
        document.getElementById("emailEdit").value = usuario.email;
        document.getElementById("perfilEdite").value = usuario.perfil;
        document.getElementById("modalEdicao").style.display = "flex";
    }
}

function fecharModais() {
    document.getElementById("modalCadastro").style.display = "none";
    document.getElementById("modalEdicao").style.display = "none";
}

function editarUsuario(id) {
    abrirModalEdicao(id);
}

function salvarUsuario() {
    let nome = document.getElementById("nomeNovo").value;
    let email = document.getElementById("emailNovo").value;
    let senha = document.getElementById("senhaNova").value;
    let perfil = document.getElementById("perfilNovo").value;

    if (!nome || !email || !senha) {
        alert("Por favor, preencha todos os campos.");
        return;
    }

    let novoUsuario = { nome, email, senha, perfil };

    fetch("http://localhost:8080/usuarios", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(novoUsuario)
    })
    .then(response => {
        if (response.status === 201) {
            alert("Usuário cadastrado com sucesso");
            fecharModais();
            document.getElementById("nomeNovo").value = "";
            document.getElementById("emailNovo").value = "";
            document.getElementById("senhaNova").value = "";
            listaUsuarios();
        } else if (response.status === 409) {
            alert("Erro: Este e-mail já está cadastrado.");
        } else {
            throw new Error("Erro ao salvar");
        }
    })
    .catch(erro => console.error("Erro ao salvar: ", erro));
}

function enviarEdicao() {
    const id = document.getElementById("idEdite").value;
    const nome = document.getElementById("nomeEdit").value;
    const email = document.getElementById("emailEdit").value;
    const perfil = document.getElementById("perfilEdite").value;

    if (!nome || !email) {
        alert("Nome e E-mail são obrigatórios.");
        return;
    }

    const usuarioEditado = { nome, email, perfil };

    fetch(`http://localhost:8080/usuarios/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(usuarioEditado)
    })
    .then(response => {
        if (response.status === 200) {
            alert("Usuário atualizado com sucesso");
            fecharModais();
            listaUsuarios();
        } else {
            throw new Error("Erro ao atualizar");
        }
    })
    .catch(erro => console.error("Erro ao editar: ", erro));
}

function desativarUsuario(id) {
    if (!confirm("Tem certeza que deseja desativar esse usuário?")) return;

    fetch(`http://localhost:8080/usuarios/${id}/desativar`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" }
    })
    .then(response => {
        if (response.status === 204) {
            listaUsuarios();
        } else {
            alert("Erro ao desativar usuário.");
        }
    })
    .catch(erro => console.error("Erro ao desativar: ", erro));
}

function ativarUsuario(id) {
    if (!confirm("Deseja reativar este usuário?")) return;

    fetch(`http://localhost:8080/usuarios/${id}/ativar`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" }
    })
    .then(response => {
        if (response.status === 204) {
            listaUsuarios();
        } else {
            alert("Erro ao ativar usuário.");
        }
    })
    .catch(erro => console.error("Erro ao ativar: ", erro));
}