package com.cardboardcritic.web.template;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

//@ConfigProperties(prefix = 'cbc.globals') FIXME: @ConfigProperties causes the app to freeze during startup
@ApplicationScoped
@Named("globals")
public class TemplateGlobals {
    @ConfigProperty(name = "cbc.globals.baseUrl")
    String baseUrl;

    @ConfigProperty(name = "cbc.globals.siteTitle")
    String siteTitle;

    public String getBaseUrl() {
        return baseUrl;
    }

    public TemplateGlobals setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getSiteTitle() {
        return siteTitle;
    }

    public TemplateGlobals setSiteTitle(String siteTitle) {
        this.siteTitle = siteTitle;
        return this;
    }
}
