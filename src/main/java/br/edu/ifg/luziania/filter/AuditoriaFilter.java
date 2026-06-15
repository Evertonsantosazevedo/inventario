package br.edu.ifg.luziania.filter;

import br.edu.ifg.luziania.model.dao.AuditoriaDAO;
import br.edu.ifg.luziania.model.entity.AuditoriaLogEntity;
import br.edu.ifg.luziania.model.entity.UsuarioEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.io.IOException;
import java.time.LocalDateTime;

@Provider
public class AuditoriaFilter implements ContainerRequestFilter {

    @Inject
    JsonWebToken jwt;

    @Inject
    AuditoriaDAO auditoriaDAO;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Pega o caminho da rota de acesso ex: /produtos
        String path = requestContext.getUriInfo().getPath();

        // Ignora recursos estáticos (CSS, JS, Imagens)
        if (path.startsWith("css") || path.startsWith("js") || path.startsWith("favicon.ico")) {
            return;
        }

        // Captura o método http
        String metodoHttp = requestContext.getMethod();

        // Ação
        String acao = metodoHttp + " /" + path;

        // Só registra se o usuário estiver logado (tiver o claim 'id')
        if (jwt != null && jwt.containsClaim("id")) {
            try {
                // Extrai o id e converte para long com segurança
                Long usuarioId = Long.parseLong(jwt.getClaim("id").toString());

                UsuarioEntity usuarioRef = new UsuarioEntity();
                usuarioRef.setId(usuarioId);

                // Instancia e popula o log
                AuditoriaLogEntity log = new AuditoriaLogEntity();
                log.setUsuario(usuarioRef);
                log.setAcao(acao);
                log.setDataHora(LocalDateTime.now());

                auditoriaDAO.salvar(log);
            } catch (Exception e) {
                // Log de erro silencioso para não interromper a requisição principal
                System.err.println("Erro ao salvar log de auditoria: " + e.getMessage());
            }
        }

    }
}
