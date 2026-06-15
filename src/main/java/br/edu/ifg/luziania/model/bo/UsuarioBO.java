package br.edu.ifg.luziania.model.bo;

import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import br.edu.ifg.luziania.model.dto.*;
import br.edu.ifg.luziania.model.entity.UsuarioEntity;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.jwt.JsonWebToken;


import java.util.List;

@RequestScoped
public class UsuarioBO {

    @Inject
    UsuarioDAO usuarioDAO;


    public void cadastrarUsuario(CadastroRequestDTO cadastroRequestDTO) {
        if (usuarioDAO.buscarPorEmail(cadastroRequestDTO.email()) != null) {
            throw new WebApplicationException("e-mail já cadastrado", 409);
        }
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome(cadastroRequestDTO.nome());
        usuario.setEmail(cadastroRequestDTO.email());
        String senhaHash = BcryptUtil.bcryptHash(cadastroRequestDTO.senha());
        usuario.setSenha(senhaHash);
        usuario.setPerfil(cadastroRequestDTO.perfil());

        usuarioDAO.cadastrar(usuario);

    }

    public AuthResultadoDTO realizarLogin(LoginRequestDTO loginRequestDTO) {

        UsuarioEntity usuario = usuarioDAO.buscarPorEmail(loginRequestDTO.email());
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
                .claim("id", usuario.getId()) // Recupera a Id do usuário com uma chamada rotulada
                .expiresIn(3600) // 1 hora em segundos
                .sign(); //Assina digitalmente e gera a string final

        return new AuthResultadoDTO(token, usuario.getNome(), usuario.getPerfil().name());

    }

    public List<UsuarioListDTO> listarTodos() {
        return usuarioDAO.listarTodos();
    }


    @Inject
    JsonWebToken jwt;

    public void desativarUsuario(Long idParaDesativar) {

        Long idUsuarioLogado = Long.valueOf(jwt.getClaim("id").toString());


        if (idParaDesativar.equals(idUsuarioLogado)) {
            throw new WebApplicationException("Operação não autorizada", 403);
        }
        UsuarioEntity usuarioAlvo = usuarioDAO.buscarPorId(idParaDesativar);
        if (usuarioAlvo == null) {
            throw new WebApplicationException("Usuário não encontrado", 404);
        }
        usuarioAlvo.setAtivo(false);

        usuarioDAO.atualizar(usuarioAlvo);
    }

    public void ativarUsuario(Long idParaAtivar) {
        UsuarioEntity usuarioAlvo = usuarioDAO.buscarPorId(idParaAtivar);
        if (usuarioAlvo == null) {
            throw new WebApplicationException("Usuário não encontrado", 404);
        }
        usuarioAlvo.setAtivo(true);
        usuarioDAO.atualizar(usuarioAlvo);
    }


    public void editarUsuario(Long id, UsuarioEdicaoDTO edicaoDTO) {

        UsuarioEntity usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new WebApplicationException("Usuário não encontrado", 404);
        }

        // Verifica se o novo e-mail já existe e se não é do próprio usuário
        UsuarioEntity usuarioComMesmoEmail = usuarioDAO.buscarPorEmail(edicaoDTO.email());
        if (usuarioComMesmoEmail != null && !usuarioComMesmoEmail.getId().equals(id)) {
            throw new WebApplicationException("Este e-mail já pertence a outro usuário", 409);
        }

        usuario.setNome(edicaoDTO.nome());
        usuario.setEmail(edicaoDTO.email());
        usuario.setPerfil(edicaoDTO.perfil());

        // Atualiza a senha se for fornecida
        if (edicaoDTO.senha() != null && !edicaoDTO.senha().isBlank()) {
            usuario.setSenha(BcryptUtil.bcryptHash(edicaoDTO.senha()));
        }

        usuarioDAO.atualizar(usuario);
    }
}
