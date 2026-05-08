package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.dto.ProdutoListDTO;
import br.edu.ifg.luziania.model.entity.Produto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class ProdutoDAO {

    @Inject
    EntityManager entityManager;

    public Produto buscarPorCodigo(String codigo){
        //language=jpql
        String hql = "SELECT p FROM Produto p where p.codigo = :codigo";
        return (Produto) entityManager
                .createQuery(hql)
                .setParameter("codigo", codigo)
                .getResultStream().findFirst()
                .orElse(null);
    }

    @Transactional
    public void cadastrar(Produto novProduto){
        entityManager.persist(novProduto);
    }

    public List<ProdutoListDTO> listarTodos(){
        //language=jpql
        String hql = "SELECT new br.edu.ifg.luziania.model.dto.ProdutoListDTO(p.id, p.codigo, p.nome, p.marca, p.quantidade, p.valorVenda) FROM Produto p";
        return (List<ProdutoListDTO>) entityManager
                .createQuery(hql)
                .getResultList();
    }

    @Transactional
    public void atualizar(Produto produto){
        entityManager.merge(produto);
    }
}
