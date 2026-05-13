package br.edu.ifg.luziania.model.bo;

import br.edu.ifg.luziania.model.dao.MovimentacaoDAO;
import br.edu.ifg.luziania.model.dao.ProdutoDAO;
import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import br.edu.ifg.luziania.model.dto.MovimentacaoListDTO;
import br.edu.ifg.luziania.model.dto.SaidaEstoqueDTO;
import br.edu.ifg.luziania.model.entity.MovimentacaoEntity;
import br.edu.ifg.luziania.model.entity.ProdutoEntity;
import br.edu.ifg.luziania.model.entity.TipoMovimentacao;
import br.edu.ifg.luziania.model.entity.UsuarioEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.time.LocalDateTime;
import java.util.List;

@RequestScoped
public class MovimentacaoBO {

    @Inject
    MovimentacaoDAO movimentacaoDAO;

    @Inject
    ProdutoDAO produtoDAO;

    @Inject
    UsuarioDAO usuarioDAO;

    public void registrarSaida(Long idProduto, Long idUsuario, SaidaEstoqueDTO saidaDTO){
       ProdutoEntity produto = produtoDAO.buscarPorId(idProduto);
       if (produto == null){
           throw new WebApplicationException("Produto não encontrado", 404);
       } else if (produto.getQuantidade() < saidaDTO.quantidade()) {
           throw new WebApplicationException("Quantidade inválida", 422);
       }
       produto.setQuantidade(produto.getQuantidade() - saidaDTO.quantidade());

       produtoDAO.atualizar(produto);

       UsuarioEntity usuario = usuarioDAO.buscarPorId(idUsuario);
        MovimentacaoEntity movimentacaoEntity = new MovimentacaoEntity();
        movimentacaoEntity.setUsuario(usuario);
        movimentacaoEntity.setProduto(produto);
        movimentacaoEntity.setQuantidade(saidaDTO.quantidade());
        movimentacaoEntity.setDataHora(LocalDateTime.now());
        movimentacaoEntity.setTipoMovimentacao(TipoMovimentacao.SAIDA);

        movimentacaoDAO.salvar(movimentacaoEntity);
    }

    public List<MovimentacaoListDTO> listarTodos(Long idProduto){
       if (idProduto == null){
           return movimentacaoDAO.listarTodos();
       }else {
           return movimentacaoDAO.listarPorProduto(idProduto);
       }
    }

}
