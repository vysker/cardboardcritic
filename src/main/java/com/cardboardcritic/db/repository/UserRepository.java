package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    @ConfigProperty(name = "cbc.password.salt")
    String salt;

    public Uni<User> create(User user) {
        user.password = BcryptUtil.bcryptHash(user.password, 10, salt.getBytes(StandardCharsets.UTF_8));
        return persist(user);
    }

    public static SecurityIdentity toSecurityIdentity(User user) {
        return new QuarkusSecurityIdentity.Builder()
                .setPrincipal(new QuarkusPrincipal(user.username))
                .addRole(user.role)
                .build();
    }
}
