window.onload = function () {
    // Procurar o perfil definido no login
    const perfil = sessionStorage.getItem('perfil');

    // Se o perfil for nulo indica que o usuário tentou pular a tela de login
    if (perfil == null) {
        alert("Acesso negado");

        //Redireciona o usuário para tela de login
        window.location.href = "/auth/login"

        //encerra a execução
        return;
    }

    if (perfil === 'ADMINISTRADOR') {
        // se for adm, removemos o display:none dos itens protegidos
        document.getElementById('menu-usuarios').style.display = 'list-item'
        document.getElementById('menu-catalogo').style.display = 'list-item'
        document.getElementById('menu-auditoria').style.display = 'list-item'
    }

}

async function fazerLogout() {
    try {
        await fetch('/auth/logout', {method: 'POST'})
    }catch (e){
        console.error("Erro ao deslogar no servidor", e)
    }

    // Remove dados do navegador
    sessionStorage.removeItem('nome')
    sessionStorage.removeItem('perfil')
    window.location.href = "/auth/login"

}