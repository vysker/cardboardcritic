package com.cardboardcritic.auth;

import com.cardboardcritic.db.repository.UserRepository;
import io.quarkus.arc.Priority;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

@ApplicationScoped
@Alternative
@Priority(999)
public class UserPassAuth implements IdentityProvider<UsernamePasswordAuthenticationRequest> {
    private static final Logger log = Logger.getLogger(UserPassAuth.class);

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
        final String requestPassword = String.valueOf(request.getPassword().getPassword());

        return userRepository.find("username = ?1", request.getUsername()).firstResult()
                .onItem().ifNull().failWith(new UnauthorizedException())
                .onItem().ifNotNull().transform(Unchecked.function(user -> {
                    if (!BcryptUtil.matches(requestPassword, user.password))
                        throw new UnauthorizedException();

                    return UserRepository.toSecurityIdentity(user);
                }));
    }
}
