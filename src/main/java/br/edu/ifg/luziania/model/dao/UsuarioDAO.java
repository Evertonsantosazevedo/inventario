package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.dto.UsuarioListDTO;
import br.edu.ifg.luziania.model.entity.UsuarioEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class UsuarioDAO {

    @Inject
    EntityManager entityManager;


    public UsuarioEntity buscarPorEmail(String email) {
        //language=jpql
        String hql = "SELECT u FROM UsuarioEntity u WHERE u.email = :email";
        return (UsuarioEntity) entityManager
                .createQuery(hql)
                .setParameter("email", email)
                .getResultStream().findFirst() //Retorna primeira opção encontrada ou null
                .orElse(null);
    }

    @Transactional
    public void cadastrar(UsuarioEntity novoUsuario) {
        entityManager.persist(novoUsuario);
    }

    public List<UsuarioListDTO> listarTodos() {
        //language=jpql
        String hql = "SELECT new br.edu.ifg.luziania.model.dto.UsuarioListDTO(u.id, u.nome, u.email, u.perfil) FROM UsuarioEntity u";
        return (List<UsuarioListDTO>) entityManager
                .createQuery(hql)
                .getResultList();
    }


    public UsuarioEntity buscarPorId(Long id) {
        //language=jpql
        String hql = "SELECT u FROM UsuarioEntity u WHERE u.id = :id";
        return (UsuarioEntity) entityManager
                .createQuery(hql)
                .setParameter("id", id)
                .getResultStream()
                .findFirst()
                .orElse(null); //Caso o ID não exista

    }

    @Transactional
    public void atualizar(UsuarioEntity usuario) {
        entityManager.merge(usuario);
    }
}
