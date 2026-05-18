function listaUsuarios() {
    // Procurar o token de acesso da secção
    const token = sessionStorage.getItem('token');
    let url = "http://localhost:8080/usuarios";

    // faz o fetch passando o token no cabeçalho
    fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        }
    )
        .then(function (response) {
            if (response.status === 200) {
                return response.json()
            } else {
                throw new Error("Sessão expirada ou erro no servidor")
            }
        })
        .then(function (listaUsuarios) {
            //chama a função para desenhar a tabela
            atualizarTabela(listaUsuarios)
        })
        .catch(function (erro) {
            console.error("Erro ao listar: ", erro)
        })
}

window.onload = function () {
    // Procurar o token de acesso da secção
    const token = sessionStorage.getItem('token');

    // Se o token for nulo indica que o usuário tentou pular a tela de login
    if (token == null) {
        alert("Acesso negado");

        //Redireciona o usuário para tela de login
        window.location.href = "/auth/login"

        //encerra a execução
        return;
    }

    listaUsuarios()
}

// Funções de formatação
//transforma o true e false do status em texto legível
function formatarStatus(ativo) {
    if (ativo === true) {
        return "Ativo"
    } else {
        return "Inativo"
    }
}

//Tranforma o Enuma ADMINISTRADOR para Administrador
function formatarPerfil(perfil) {
    if (perfil == null) return ""
    return perfil.charAt(0).toUpperCase() + perfil.slice(1).toLowerCase()
}

// Renderização da tabela
function atualizarTabela(usuarios) {
    let tabela = document.getElementById("tabelaUsuarios")

    //limpa a tabela antes de preencher para não duplicar dados
    tabela.innerHTML = "";

    for (let i = 0; i < usuarios.length; i++) {
        let u = usuarios[i]

        //Monta a linha <tr> concatenando as células <td> com os dados do usuário
        let linha = "<tr>" +
            "<td>" + u.id + "</td>" +
            "<td>" + u.email + "</td>" +
            "<td>" + formatarPerfil(u.perfil) + "</td>" +
            "<td>" + formatarStatus(u.ativo) + "</td>" +
            "<td>" +
            "<button onclick='editarUsuario(" + u.id + ")'>Editar</button>" +
            "<button onclick='desativarUsuario(" + u.id + ")'>Desativar</button>" +
            "</td>" +
            "</tr>"

        tabela.innerHTML += linha;
    }
}
