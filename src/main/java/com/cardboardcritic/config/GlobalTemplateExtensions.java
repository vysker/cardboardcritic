package com.cardboardcritic.config;

import io.quarkus.arc.Arc;
import io.quarkus.qute.RawString;
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

    @TemplateExtension(namespace = "score")
    public static String name(int score) {
        if (score < 30) return "flat";
        if (score < 60) return "fine";
        if (score < 90) return "good";
        return "best";
    }

    @TemplateExtension(namespace = "score")
    public static String color(int score) {
        return "var(--score-%s-color)".formatted(name(score));
    }

    // This is needed for native images, because it thinks ".startsWith" is unused, so it strips it out of the build
    @TemplateExtension(namespace = "")
    public static boolean startsWith(String subject, String prefix) {
        return subject.startsWith(prefix);
    }

    // This is needed for native images, because it thinks ".raw" is unused, so it strips it out of the build
    @TemplateExtension(namespace = "")
    public static RawString raw(String subject) {
        return new RawString(subject);
    }

    // This is needed for native images, because it thinks ".replaceAll" is unused, so it strips it out of the build
    @TemplateExtension(namespace = "")
    public static String replaceAll(String subject, String regex, String replacement) {
        return subject.replaceAll(regex, replacement);
    }

    @TemplateExtension(namespace = "")
    public static int add(int subject, int amount) {
        return subject + amount;
    }
}
