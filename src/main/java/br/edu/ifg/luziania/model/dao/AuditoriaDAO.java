package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.dto.AuditoriaListDTO;
import br.edu.ifg.luziania.model.entity.AuditoriaLogEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class AuditoriaDAO {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void salvar(AuditoriaLogEntity log){
        entityManager.persist(log);
    }

    public List<AuditoriaListDTO> listarTodos() {
        //language=jpql
        String hql = "SELECT new br.edu.ifg.luziania.model.dto.AuditoriaListDTO(" +
                "a.id, u.nome, u.email, a.acao, a.dataHora) " +
                "FROM AuditoriaLogEntity a " +
                "JOIN a.usuario u " +
                "ORDER BY a.dataHora DESC";
        return entityManager.createQuery(hql, AuditoriaListDTO.class).getResultList();
    }
}

