package com.cardboardcritic.auth;

import com.cardboardcritic.db.entity.User;
import com.cardboardcritic.db.repository.UserRepository;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TrustedAuthenticationRequest;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Alternative
@Priority(999)
public class TrustedAuth implements IdentityProvider<TrustedAuthenticationRequest> {

    @Inject
    UserRepository userRepository;

    @Override
    public Class<TrustedAuthenticationRequest> getRequestType() {
        return TrustedAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(TrustedAuthenticationRequest request,
                                              AuthenticationRequestContext context) {
        final String username = request.getPrincipal();

        return context.runBlocking(() -> getSecurityIdentity(username));
    }

    @Transactional
    public SecurityIdentity getSecurityIdentity(String username) {
        final User user = userRepository.find("username = ?1", username).firstResult();
        if (user == null)
            throw new UnauthorizedException();

        return UserRepository.toSecurityIdentity(user);
    }
}
