package com.cardboardcritic.web.template;

import lombok.Data;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Data
//@ConfigProperties(prefix = 'cbc.globals') FIXME: @ConfigProperties causes the app to freeze during startup
@ApplicationScoped
@Named("globals")
public class TemplateGlobals {
    @ConfigProperty(name = "cbc.globals.baseUrl")
    private String baseUrl;

    @ConfigProperty(name = "cbc.globals.siteTitle")
    private String siteTitle;
}
