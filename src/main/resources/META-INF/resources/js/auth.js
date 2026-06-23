async function fazerLogin() {
    const emailDigitado = document.getElementById('e-mail').value
    const senhaDigitada = document.getElementById('senha').value

    const loginRequestDTO = {
        email: emailDigitado,
        senha: senhaDigitada
    };

    try {
        const resposta = await fetch('/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginRequestDTO)
        });
        if (!resposta.ok) {
            alert("Credenciais inválidas");
            return;
        }
        const dados = await resposta.json();
        sessionStorage.setItem('nome', dados.nome);
        sessionStorage.setItem('perfil', dados.perfil);

        window.location.href = "/dashboard"
    } catch (erro) {
        console.error("Erro ao conectar com o servidor", erro);
        alert("O servidor está fora do ar.")
    }
}