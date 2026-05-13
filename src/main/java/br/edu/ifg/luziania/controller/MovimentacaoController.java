package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.bo.MovimentacaoBO;
import br.edu.ifg.luziania.model.dto.SaidaEstoqueDTO;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/movimentacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovimentacaoController {

    @Inject
    MovimentacaoBO movimentacaoBO;

    @Inject
    JsonWebToken jwt;


    @Path("/saida")
    @POST
    // @Authenticated // Qualquer um com autenticação pode acessar essa rota
    @RolesAllowed({"ADMINISTRADOR", "OPERADOR"})
    public Response saidaProdutos(@QueryParam("idProduto") Long idProduto, @Valid SaidaEstoqueDTO saidaEstoqueDTO) {
        Long idUsuario = Long.valueOf(jwt.getClaim("id").toString());
        movimentacaoBO.registrarSaida(idProduto, idUsuario, saidaEstoqueDTO);

        return Response.noContent().build();
    }

    @GET
    @RolesAllowed({"ADMINISTRADOR", "OPERADOR"})
    public Response listarMovimentacoes(@QueryParam("idProduto") Long idProduto){
        return Response.ok(movimentacaoBO.listarTodos(idProduto)).build();
    }
}
