package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.entity.AuditoriaLogEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@RequestScoped
public class AuditoriaDAO {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void salvar(AuditoriaLogEntity log){
        entityManager.persist(log);
    }


}
