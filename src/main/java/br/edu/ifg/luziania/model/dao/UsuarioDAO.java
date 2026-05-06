package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.dto.UsuarioListDTO;
import br.edu.ifg.luziania.model.entity.Usuario;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jdk.dynalink.linker.LinkerServices;

import java.util.List;

@RequestScoped
public class UsuarioDAO {

    @Inject
    EntityManager entityManager;


    public Usuario buscarPorEmail(String email){
        //language=jpql
        String hql = "SELECT u FROM Usuario u WHERE u.email = :email";
        return (Usuario) entityManager
                .createQuery(hql)
                .setParameter("email", email)
                .getResultStream().findFirst() //Retorna primeira opção encontrada ou null
                .orElse(null);
    }

    @Transactional
    public void cadastrar(Usuario novoUsuario){
        entityManager.persist(novoUsuario);
    }

    /*
    String hql = "SELECT new br.edu.ifg.luziania.model.dto.UsuarioListDTO(u.nome, u.email, u.perfil) FROM Usuario u";
return (List<UsuarioListDTO>) entityManager.createQuery(hql).getResultList();
     */
    public List<UsuarioListDTO> listarTodos(){
        //language=jpql
        String hql = "SELECT new br.edu.ifg.luziania.model.dto.UsuarioListDTO(u.id, u.nome, u.email, u.perfil) FROM Usuario u";
        return (List<UsuarioListDTO>) entityManager
                .createQuery(hql)
                .getResultList();
    }
}
