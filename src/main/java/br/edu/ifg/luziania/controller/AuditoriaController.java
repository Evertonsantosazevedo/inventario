package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.bo.AuditoriaBO;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auditoria")
@RolesAllowed("ADMINISTRADOR")
public class AuditoriaController {

    @Inject
    AuditoriaBO auditoriaBO;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance auditoria();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance telaAuditoria() {
        return Templates.auditoria();
    }

    @GET
    @Path("/dados")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarAuditoria() {
        return Response.ok(auditoriaBO.listarTodos()).build();
    }
}
