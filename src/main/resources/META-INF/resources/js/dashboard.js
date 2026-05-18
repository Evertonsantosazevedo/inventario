window.onload = function (){
    // Procurar o token de acesso da secção
    const token = sessionStorage.getItem('token');

    // Se o token for nulo indica que o usuário tentou pular a tela de login
    if (token == null){
        alert("Acesso negado");

        //Redireciona o usuário para tela de login
        window.location.href = "/auth/login"

        //encerra a execução
        return;
    }
    //Lógica do menu dinâmico
    const pefil = sessionStorage.getItem('perfil')

    if (pefil === 'ADMINISTRADOR'){
        // se for adm, removemos o display:none dos itens protegidos
        document.getElementById('menu-usuarios').style.display = 'list-item'
        document.getElementById('menu-catalogo').style.display = 'list-item'
        document.getElementById('menu-auditoria').style.display = 'list-item'
    }

}

function fazerLogout(){
    //Remove token e perfil do navegador
    sessionStorage.removeItem('token')
    sessionStorage.removeItem('perfil')

    // Joga o usuário de volta para tela de login
    window.location.href = "/auth/login"
}