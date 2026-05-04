package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.entity.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioDAO implements PanacheRepository<Usuario> {

    /***
     *
     * @param email
     * @return primeira ocorrência do param
     */
    public Usuario buscaPorEmail(String email){
        return find("email", email).firstResult();
    }
}
