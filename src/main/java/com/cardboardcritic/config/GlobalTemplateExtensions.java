package com.cardboardcritic.config;

import io.quarkus.arc.Arc;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.security.identity.SecurityIdentity;
import org.jboss.logging.Logger;

import java.util.List;

@TemplateExtension(namespace = "global")
public class GlobalTemplateExtensions {
    public static final String ERRORS_ATTRIBUTE = "errors";

    private static final Logger log = Logger.getLogger(GlobalTemplateExtensions.class);

    @SuppressWarnings("unchecked")
    public static List<String> errors(@TemplateExtension.TemplateAttribute("errors") Object errors) {
        if (errors == null)
            return null;
        if (errors instanceof List)
            return (List<String>) errors;
        if (errors instanceof String)
            return List.of(errors.toString());

        log.warn("Errors attribute was set, but was not of type List or String");
        return null;
    }

    public static String username() {
        final SecurityIdentity identity = Arc.container().instance(SecurityIdentity.class).get();
        if (identity == null)
            return null;
        return identity.getPrincipal().getName();
    }

    @TemplateExtension(namespace = "")
    public static String uppercaseFirst(String subject) {
        return subject.substring(0, 1).toUpperCase() + subject.substring(1);
    }

    // This is needed for native images, because otherwise it will strip out String.startsWith during compilation
    @TemplateExtension(namespace = "")
    public static boolean startsWith(String subject, String prefix) {
        return subject.startsWith(prefix);
    }

    @TemplateExtension(namespace = "")
    public static int add(int subject, int amount) {
        return subject + amount;
    }
}
