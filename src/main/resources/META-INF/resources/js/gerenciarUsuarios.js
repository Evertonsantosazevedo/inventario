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
            "<td>" + u.nome + "</td>" +
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

//Função de cadastro de usuário
function salvarUsuario() {
    //Captura os dados digitados no formulário
    let nomeDigitado = document.getElementById("nomeNovo").value;
    let emailDigitado = document.getElementById("emailNovo").value
    let senhaDigitada = document.getElementById("senhaNova").value
    let perfilSelecionado = document.getElementById("perfilNovo").value

    // Validação simples para não enviar dados vazios
    if (nomeDigitado === "" || emailDigitado === "" || senhaDigitada === "") {
        alert("Por favor, preencha todos os campos.")
        return
    }
    if (senhaDigitada.length < 8) {
        alert("A senha deve ter no mínimo 8 caracteres.")
        return
    }

    // Monta o DTO que será enviado no corpo da requisição
    let novoUsuario = {
        nome: nomeDigitado,
        email: emailDigitado,
        senha: senhaDigitada,
        perfil: perfilSelecionado
    }

    const token = sessionStorage.getItem('token')
    let url = "http://localhost:8080/usuarios"

    // Faz o fetch passando o token no cabeçalho e os dados no body
    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify(novoUsuario)
    })
        .then(function (response) {
            // trata as resposta com base nos códigos https devolvidos
            if (response.status === 201) {
                alert("Usuário cadastrado com sucesso")

                //Limpa os campos do formulário
                document.getElementById("nomeNovo").value = ""
                document.getElementById("emailNovo").value = ""
                document.getElementById("senhaNova").value = ""

                //Chama listarUsuarios() para atualizar a tabela automaticamente
                listaUsuarios()
            } else if (response.status === 409) {
                alert("Erro: Este e-mail já está cadastrado.")
            } else if (response.status === 400) {
                alert("Erro: Dados inválidos. Verifique os campos")
            } else {
                throw new Error("Erro no servidor ao tentar salvar.")
            }
        })
        .catch(function (erro) {
            console.error("Erro ao salvar usuário: ", erro)
        })
}

function desativarUsuario(id) {
    // confirmação de segurança para o caso de click acidentais
    const confirmacao = confirm("Tem certeza que deseja desativar esse usuário?")
    if (!confirmacao) {
        return // interrompe a operação se o usuário cancelar
    }

    const token = sessionStorage.getItem('token')
    let url = `http://localhost:8080/usuarios/${id}/desativar`

    fetch(url, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        }
    })
        .then(function (response) {
                if (response.status === 204) {
                    console.log(`Usuário ${id} desativado com sucesso.`)
                    listaUsuarios()
                } else if (response.status === 401) {
                    alert("Sua sessão expirou. Faça login novamente.");
                    window.location.href = "/auth/login"
                } else if (response.status === 403) {
                    alert("Ação negada: Você não tem permissão ou não pode desativar a si mesmo.")
                } else if (response.status === 404) {
                    alert("Erro: O usuário não foi encontrado no sistema.")
                } else {
                    throw new Error("Erro no servidor ao tentar salvar.")
                }
            }
        )
        .catch(function (erro) {
            console.error("Erro ao desativar usuário: ", erro)
        })
}