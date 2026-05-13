package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.dto.MovimentacaoListDTO;
import br.edu.ifg.luziania.model.entity.MovimentacaoEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class MovimentacaoDAO {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void salvar(MovimentacaoEntity movimentacao){
        entityManager.persist(movimentacao);
    }

    public List<MovimentacaoListDTO> listarTodos(){
        //language=jpql
        String hql = "SELECT new br.edu.ifg.luziania.model.dto.MovimentacaoListDTO " +
                "(m.id, p.nome, p.marca, u.nome, m.quantidade, m.tipoMovimentacao, m.dataHora) " +
                "FROM MovimentacaoEntity m " +
                "JOIN m.produto p " +
                "JOIN m.usuario u " +
                "ORDER BY m.dataHora DESC";

        return (List<MovimentacaoListDTO>) entityManager
                .createQuery(hql)
                .getResultList();

    }

    public List<MovimentacaoListDTO> listarPorProduto(Long idProduto){
        //language=jpql
        String hql = "SELECT new br.edu.ifg.luziania.model.dto.MovimentacaoListDTO" +
                "(m.id, p.nome, p.marca, u.nome, m.quantidade, m.tipoMovimentacao, m.dataHora) " +
                "FROM MovimentacaoEntity m " +
                "JOIN m.produto p " +
                "JOIN m.usuario u "+
                "WHERE p.id = :idProduto " +
                "ORDER BY m.dataHora DESC";

        return (List<MovimentacaoListDTO>) entityManager
                .createQuery(hql)
                .setParameter("idProduto", idProduto)
                .getResultList();
    }
}
