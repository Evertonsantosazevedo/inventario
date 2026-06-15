package br.edu.ifg.luziania.model.bo;

import br.edu.ifg.luziania.model.dao.MovimentacaoDAO;
import br.edu.ifg.luziania.model.dao.ProdutoDAO;
import br.edu.ifg.luziania.model.dto.EntradaEstoqueRequestDTO;
import br.edu.ifg.luziania.model.dto.ProdutoCadastroRequestDTO;
import br.edu.ifg.luziania.model.dto.ProdutoEdicaoDTO;
import br.edu.ifg.luziania.model.dto.ProdutoListDTO;
import br.edu.ifg.luziania.model.entity.MovimentacaoEntity;
import br.edu.ifg.luziania.model.entity.ProdutoEntity;
import br.edu.ifg.luziania.model.entity.TipoMovimentacao;
import br.edu.ifg.luziania.model.entity.UsuarioEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.LocalDateTime;
import java.util.List;

@RequestScoped
public class ProdutoBO {

    @Inject
    ProdutoDAO produtoDAO;

    @Inject
    MovimentacaoDAO movimentacaoDAO;

    @Inject
    JsonWebToken jwt;

    public void cadastrarProduto(ProdutoCadastroRequestDTO requestDTO) {
        if (produtoDAO.buscarPorCodigo(requestDTO.codigo()) != null) {
            throw new WebApplicationException("Produto já cadastrado", 409);
        }
        ProdutoEntity produto = new ProdutoEntity();
        produto.setCodigo(requestDTO.codigo());
        produto.setNome(requestDTO.nome());
        produto.setMarca(requestDTO.marca());
        produto.setQuantidade(requestDTO.quantidade());
        produto.setValorVenda(requestDTO.valorVenda());

        produtoDAO.cadastrar(produto);
    }

    public List<ProdutoListDTO> listarTodos() {
        return produtoDAO.listarTodos();
    }

    public void atualizarProduto(Long id, ProdutoEdicaoDTO edicaoDTO) {
        ProdutoEntity produtoAlvo = produtoDAO.buscarPorId(id);

        if (produtoAlvo == null) {
            throw new WebApplicationException("Produto não encontrado", 404);
        }
        produtoAlvo.setCodigo(edicaoDTO.codigo());
        produtoAlvo.setNome(edicaoDTO.nome());
        produtoAlvo.setMarca(edicaoDTO.marca());
        produtoAlvo.setValorVenda(edicaoDTO.valorVenda());

        produtoDAO.atualizar(produtoAlvo);

    }

    public void registrarEntrada(Long id, EntradaEstoqueRequestDTO requestDTO) {
        ProdutoEntity produtoAlvo = produtoDAO.buscarPorId(id);
        if (produtoAlvo == null) {
            throw new WebApplicationException("Produto não encontrado", 404);
        }
        produtoAlvo.setQuantidade(produtoAlvo.getQuantidade() + requestDTO.quantidade());

        produtoDAO.atualizar(produtoAlvo);

        // Registro no histórico de movimentações
        MovimentacaoEntity movimentacao = new MovimentacaoEntity();
        movimentacao.setProduto(produtoAlvo);
        movimentacao.setQuantidade(requestDTO.quantidade());
        movimentacao.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        movimentacao.setDataHora(LocalDateTime.now());

        // Extrai o usuário logado do JWT
        if (jwt != null && jwt.containsClaim("id")) {
            Long idUsuarioLogado = Long.valueOf(jwt.getClaim("id").toString());
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setId(idUsuarioLogado);
            movimentacao.setUsuario(usuario);
        }

        movimentacaoDAO.salvar(movimentacao);
    }

    public void deletarProduto(Long id) {
        ProdutoEntity produto = produtoDAO.buscarPorId(id);
        if (produto == null) {
            throw new WebApplicationException("Produto não encontrado", 404);
        }

        // Verifica se há movimentações vinculadas antes de deletar
        if (!movimentacaoDAO.listarPorProduto(id).isEmpty()) {
            throw new WebApplicationException("Não é possível deletar um produto que possui movimentações", 409);
        }

        produtoDAO.deletar(id);
    }

}
