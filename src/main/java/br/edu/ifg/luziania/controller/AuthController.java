package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.bo.UsuarioBO;
import br.edu.ifg.luziania.model.dto.AuthResultadoDTO;
import br.edu.ifg.luziania.model.dto.LoginRequestDTO;
import br.edu.ifg.luziania.model.dto.LoginResponseDTO;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    UsuarioBO usuarioBO;

    @CheckedTemplate
    public static class Templates {
        // O Quarkus vai procurar automaticamente o arquivo:
        // src/main/resources/templates/AuthController/login.html
        public static native TemplateInstance login();

        public static native TemplateInstance dashboard();
    }


    //Retorna ao cliente web o html com a tela de login
    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance telaLogin() {
        return Templates.login();
    }


    @GET
    @Path("/dashboard")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance telaDashboard() {
        return Templates.dashboard();
    }

    @Path("/login")
    @POST
    public Response login(@Valid LoginRequestDTO loginRequestDTO) {

        AuthResultadoDTO authResultado = usuarioBO.realizarLogin(loginRequestDTO);


        NewCookie jwtCookie = new NewCookie.Builder("jwt")
                .value(authResultado.token())
                .path("/") // Válido para toda aplicação
                .httpOnly(true)  //Impede acesso via java script
                .secure(true) //HTTPS
                .sameSite(NewCookie.SameSite.STRICT)
                .maxAge(3600)
                .build();

        //Dto limpo para o front sem o token
        LoginResponseDTO responseDTO = new LoginResponseDTO(
                authResultado.nome(),
                authResultado.perfil()
        );

        // retorna o status de ok, com o cookie no body

        return Response.ok(responseDTO)
                .cookie(jwtCookie)
                .build();

    }


}
