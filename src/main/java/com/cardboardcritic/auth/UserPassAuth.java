package com.cardboardcritic.auth;

import com.cardboardcritic.db.entity.User;
import com.cardboardcritic.db.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Alternative
@Priority(999)
public class UserPassAuth implements IdentityProvider<UsernamePasswordAuthenticationRequest> {

    @Inject
    UserRepository userRepository;

    @Override
    public Class<UsernamePasswordAuthenticationRequest> getRequestType() {
        return UsernamePasswordAuthenticationRequest.class;
    }

    @Override
    @ActivateRequestContext
    public Uni<SecurityIdentity> authenticate(UsernamePasswordAuthenticationRequest request,
                                              AuthenticationRequestContext context) {
        final String username = request.getUsername();
        final String requestPassword = String.valueOf(request.getPassword().getPassword());

        return context.runBlocking(() -> getSecurityIdentity(username, requestPassword));
    }

    @Transactional
    public SecurityIdentity getSecurityIdentity(String username, String requestPassword) {
        final User user = userRepository.find("username = ?1", username).firstResult();
        if (user == null)
            throw new UnauthorizedException();

        if (!BcryptUtil.matches(requestPassword, user.password))
            throw new UnauthorizedException();

        return UserRepository.toSecurityIdentity(user);
    }
}
