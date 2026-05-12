package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.dto.ProdutoListDTO;
import br.edu.ifg.luziania.model.entity.ProdutoEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class ProdutoDAO {

    @Inject
    EntityManager entityManager;

    public ProdutoEntity buscarPorId(Long id){
        //language=jpql
        String hql = "SELECT p FROM ProdutoEntity p WHERE p.id = :id";
        return (ProdutoEntity) entityManager
                .createQuery(hql)
                .setParameter("id", id)
                .getResultStream().findFirst()
                .orElse(null);
    }

    public ProdutoEntity buscarPorCodigo(String codigo){
        //language=jpql
        String hql = "SELECT p FROM ProdutoEntity p where p.codigo = :codigo";
        return (ProdutoEntity) entityManager
                .createQuery(hql)
                .setParameter("codigo", codigo)
                .getResultStream().findFirst()
                .orElse(null);
    }

    @Transactional
    public void cadastrar(ProdutoEntity novProduto){
        entityManager.persist(novProduto);
    }

    public List<ProdutoListDTO> listarTodos(){
        //language=jpql
        String hql = "SELECT new br.edu.ifg.luziania.model.dto.ProdutoListDTO(p.id, p.codigo, p.nome, p.marca, p.quantidade, p.valorVenda) FROM ProdutoEntity p";
        return (List<ProdutoListDTO>) entityManager
                .createQuery(hql)
                .getResultList();
    }

    @Transactional
    public void atualizar(ProdutoEntity produto){
        entityManager.merge(produto);
    }
}
