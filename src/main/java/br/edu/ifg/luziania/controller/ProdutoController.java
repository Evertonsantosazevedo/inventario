package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.bo.ProdutoBO;
import br.edu.ifg.luziania.model.dto.EntradaEstoqueRequestDTO;
import br.edu.ifg.luziania.model.dto.ProdutoCadastroRequestDTO;
import br.edu.ifg.luziania.model.dto.ProdutoEdicaoDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
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

    @GET
    @RolesAllowed("ADMINISTRADOR")
    public Response listarProdutos() {
        return Response.ok(produtoBO.listarTodos()).build();
    }

    @Path("/{id}")
    @PUT
    @RolesAllowed("ADMINISTRADOR")
    public Response atualizarProdutos(@PathParam("id") Long id, @Valid ProdutoEdicaoDTO requestDTO) {
        produtoBO.atualizarProduto(id, requestDTO);

        return Response.noContent().build();
    }

    @Path("/{id}/entrada")
    @POST
    @RolesAllowed("ADMINISTRADOR")
    public Response entradaEstoque(@PathParam("id") Long id, @Valid EntradaEstoqueRequestDTO requestDTO) {
        produtoBO.registrarEntrada(id, requestDTO);

        return Response.noContent().build();
    }
}
