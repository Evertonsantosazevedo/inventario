package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.bo.ProdutoBO;
import br.edu.ifg.luziania.model.dto.ProdutoCadastroRequestDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoController {

    @Inject
    ProdutoBO produtoBO;

    @POST
    @RolesAllowed("ADMINISTRADOR")
    public Response cadastrarProduto(@Valid ProdutoCadastroRequestDTO requestDTO) {
        produtoBO.cadastrarProduto(requestDTO);
        return Response.status(Response.Status.CREATED).build();
    }
}
