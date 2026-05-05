package br.edu.ifg.luziania.model.bo;

import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import br.edu.ifg.luziania.model.dto.LoginRequestDTO;
import br.edu.ifg.luziania.model.dto.LoginResponseDTO;
import br.edu.ifg.luziania.model.entity.Usuario;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

@RequestScoped
public class UsuarioBO {

    @Inject
    UsuarioDAO usuarioDAO;


    public LoginResponseDTO realizarLogin(LoginRequestDTO loginRequestDTO) {


        Usuario usuario = usuarioDAO.buscarPorEmail(loginRequestDTO.email());
        if (usuario == null || !usuario.isAtivo()) {
            throw new WebApplicationException("login inválido", 401);
        }

        boolean senha = BcryptUtil.matches(loginRequestDTO.senha(), usuario.getSenha());

        if (!senha) {
            throw new WebApplicationException("login inválido", 401);
        }

        String token = Jwt.issuer("https://inventario.ifg.br")
                .upn(usuario.getEmail()) //email do usuário logado
                .groups(usuario.getPerfil().name()) // Perfil do usuário, grupo que ele faz parte
                .expiresIn(3600) // 1 hora em segundos
                .sign(); //Assina digitalmente e gera a string final

        return new LoginResponseDTO(token, usuario.getNome(), usuario.getPerfil().name());

    }
}
