package br.edu.ifg.luziania.model.bo;

import br.edu.ifg.luziania.model.dao.ProdutoDAO;
import br.edu.ifg.luziania.model.dto.EntradaEstoqueRequestDTO;
import br.edu.ifg.luziania.model.dto.ProdutoCadastroRequestDTO;
import br.edu.ifg.luziania.model.dto.ProdutoEdicaoDTO;
import br.edu.ifg.luziania.model.dto.ProdutoListDTO;
import br.edu.ifg.luziania.model.entity.ProdutoEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;

@RequestScoped
public class ProdutoBO {

    @Inject
    ProdutoDAO produtoDAO;

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
    }

}
