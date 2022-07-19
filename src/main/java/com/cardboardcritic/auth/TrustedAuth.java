package com.cardboardcritic.auth;

import io.quarkus.arc.Priority;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TrustedAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

@ApplicationScoped
@Alternative
@Priority(999)
public class TrustedAuth implements IdentityProvider<TrustedAuthenticationRequest> {
    private static final Logger log = Logger.getLogger(TrustedAuth.class);

//    @Inject
//    UserRepository userRepository;

    @Override
    public Class<TrustedAuthenticationRequest> getRequestType() {
        return TrustedAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(TrustedAuthenticationRequest request, AuthenticationRequestContext context) {
        final var identity = QuarkusSecurityIdentity.builder()
                .setPrincipal(new QuarkusPrincipal(request.getPrincipal()))
                .addRole("admin")
                .build();
        return Uni.createFrom().item(identity);

        // FIXME: For some reason, this causes a timeout
//        return userRepository.find("username = ?1", request.getPrincipal()).firstResult()
//                .onItem().ifNull().failWith(new UnauthorizedException())
//                .onItem().ifNotNull().transform(UserRepository::toSecurityIdentity);
    }
}
