package com.cardboardcritic.auth;

import com.cardboardcritic.db.entity.User;
import com.cardboardcritic.db.repository.UserRepository;
import io.quarkus.arc.Priority;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.transaction.Transactional;

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
