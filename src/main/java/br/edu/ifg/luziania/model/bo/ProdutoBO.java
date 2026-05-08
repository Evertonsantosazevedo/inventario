package br.edu.ifg.luziania.model.bo;

import br.edu.ifg.luziania.model.dao.ProdutoDAO;
import br.edu.ifg.luziania.model.dto.ProdutoCadastroRequestDTO;
import br.edu.ifg.luziania.model.dto.ProdutoEdicaoDTO;
import br.edu.ifg.luziania.model.dto.ProdutoListDTO;
import br.edu.ifg.luziania.model.entity.Produto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;

@RequestScoped
public class ProdutoBO {

    @Inject
    ProdutoDAO produtoDAO;

    public void cadastrarProduto(ProdutoCadastroRequestDTO requestDTO){
        if (produtoDAO.buscarPorCodigo(requestDTO.codigo()) != null){
            throw new WebApplicationException("Produto já cadastrado", 409);
        }
        Produto produto = new Produto();
        produto.setCodigo(requestDTO.codigo());
        produto.setNome(requestDTO.nome());
        produto.setMarca(requestDTO.marca());
        produto.setQuantidade(requestDTO.quantidade());
        produto.setValorVenda(requestDTO.valorVenda());

        produtoDAO.cadastrar(produto);
    }

    public List<ProdutoListDTO> listarTodos(){
        return produtoDAO.listarTodos();
    }

    public void atualizarProduto(String codigo, ProdutoEdicaoDTO edicaoDTO){
        Produto produtoAlvo = produtoDAO.buscarPorCodigo(codigo);
        if (produtoAlvo == null){
            throw new WebApplicationException("Produto não encontrado", 404);
        }


    }

}
