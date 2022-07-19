package com.cardboardcritic.web;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("auth")
public class AuthResource {
    private static final Logger log = Logger.getLogger(AuthResource.class);

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance login();
    }

    @GET
    @Path("login")
    @Produces(MediaType.TEXT_HTML)
    @PermitAll
    public Uni<TemplateInstance> login() {
        return Uni.createFrom().item(Templates.login());
    }

    @GET
    @Path("logout")
    @Produces(MediaType.TEXT_HTML)
    @PermitAll
    public Response logout() {
        return Response.seeOther(URI.create("/"))
                .header("Set-Cookie", "quarkus-credential=; path=/")
                .build();
    }
}
