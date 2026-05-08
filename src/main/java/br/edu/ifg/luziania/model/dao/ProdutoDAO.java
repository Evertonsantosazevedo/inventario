package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.entity.Produto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

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
}
