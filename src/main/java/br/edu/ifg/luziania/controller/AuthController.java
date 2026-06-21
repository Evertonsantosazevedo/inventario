package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.bo.AuditoriaBO;
import br.edu.ifg.luziania.model.bo.UsuarioBO;
import br.edu.ifg.luziania.model.dto.AuthResultadoDTO;
import br.edu.ifg.luziania.model.dto.LoginRequestDTO;
import br.edu.ifg.luziania.model.dto.LoginResponseDTO;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthController {

    @Inject
    UsuarioBO usuarioBO;

    @Inject
    AuditoriaBO auditoriaBO;

    @CheckedTemplate
    public static class Templates {
        // O Quarkus vai procurar automaticamente o arquivo:
        // src/main/resources/templates/AuthController/login.html
        public static native TemplateInstance login();

        public static native TemplateInstance dashboard();

        public static native TemplateInstance auditoria();
    }


    //Retorna ao cliente web o html com a tela de login
    @GET
    @Path("/login")
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance telaLogin() {
        return Templates.login();
    }


    @GET
    @Path("/dashboard")
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance telaDashboard() {
        return Templates.dashboard();
    }

    @GET
    @Path("/auditoria")
    @RolesAllowed("ADMINISTRADOR")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance telaAuditoria() {
        return Templates.auditoria();
    }

    @GET
    @Path("/auditoria/dados")
    @RolesAllowed("ADMINISTRADOR")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarAuditoria() {
        return Response.ok(auditoriaBO.listarTodos()).build();
    }

    @Path("/login")
    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid LoginRequestDTO loginRequestDTO) {

        AuthResultadoDTO authResultado = usuarioBO.realizarLogin(loginRequestDTO);


        NewCookie jwtCookie = new NewCookie.Builder("jwt")
                .value(authResultado.token())
                .path("/") // Válido para toda aplicação
                .httpOnly(true)  //Impede acesso via java script
                .secure(false) // Desabilitado para testes locais (HTTP)
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

    @Path("/logout")
    @POST
    @PermitAll
    public Response logout(){
        NewCookie limpaCookie = new NewCookie.Builder("jwt")
                .value("")
                .path("/")
                .maxAge(0) // Expira imediatamente
                .httpOnly(true)
                .build();

        return Response.noContent()
                .cookie(limpaCookie)
                .build();
    }


}
