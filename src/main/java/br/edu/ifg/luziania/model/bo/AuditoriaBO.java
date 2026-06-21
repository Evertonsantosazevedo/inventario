package br.edu.ifg.luziania.model.bo;

import br.edu.ifg.luziania.model.dao.AuditoriaDAO;
import br.edu.ifg.luziania.model.dto.AuditoriaListDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@RequestScoped
public class AuditoriaBO {

    @Inject
    AuditoriaDAO auditoriaDAO;

    public List<AuditoriaListDTO> listarTodos() {
        return auditoriaDAO.listarTodos();
    }
}
