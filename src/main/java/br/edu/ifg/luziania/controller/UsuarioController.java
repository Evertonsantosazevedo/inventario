package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.bo.UsuarioBO;
import br.edu.ifg.luziania.model.dto.CadastroRequestDTO;
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
