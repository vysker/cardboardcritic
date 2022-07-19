package com.cardboardcritic.auth;

import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.FormAuthenticationMechanism;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import java.util.Set;

/**
 * This is a workaround for the fact that we cannot use FormAuthenticationMechanism directly in a reactive environment.
 * The @ActivateRequestContext is crucial in that regard. Without it, we'll get errors like ContextNotActiveException.
 */
@ApplicationScoped
public class FormBasedAuth implements HttpAuthenticationMechanism {

    @Inject
    FormAuthenticationMechanism formAuthenticationMechanism;

    @Override
    @ActivateRequestContext
    public Uni<SecurityIdentity> authenticate(RoutingContext context, IdentityProviderManager identityProviderManager) {
        return formAuthenticationMechanism.authenticate(context, identityProviderManager);
    }

    @Override
    @ActivateRequestContext
    public Uni<ChallengeData> getChallenge(RoutingContext context) {
        return formAuthenticationMechanism.getChallenge(context);
    }

    @Override
    public Set<Class<? extends AuthenticationRequest>> getCredentialTypes() {
        return formAuthenticationMechanism.getCredentialTypes();
    }
}
