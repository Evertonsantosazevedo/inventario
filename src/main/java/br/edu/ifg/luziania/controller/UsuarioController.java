package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.bo.UsuarioBO;
import br.edu.ifg.luziania.model.dto.CadastroRequestDTO;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioController {

    @Inject
    UsuarioBO usuarioBO;

    @CheckedTemplate
    public static class Templates{
        public static native TemplateInstance gerenciarUsuarios();
    }

    @GET
    @Path("/gerenciar")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance telaGerenciarUsuarios(){
        return Templates.gerenciarUsuarios();
    }

    @POST
    @RolesAllowed("ADMINISTRADOR")
    public Response cadastrarUsuario(@Valid CadastroRequestDTO requestDTO) {
        usuarioBO.cadastrarUsuario(requestDTO);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @RolesAllowed("ADMINISTRADOR")
    public Response listarUsuarios() {
        return Response.ok(usuarioBO.listarTodos()).build();
    }

    @Path("/{id}/desativar")
    @PATCH
    @RolesAllowed("ADMINISTRADOR")
    public Response desativarUsuario(@PathParam("id") Long id){
        usuarioBO.desativarUsuario(id);

        return Response.noContent().build();
    }
}
