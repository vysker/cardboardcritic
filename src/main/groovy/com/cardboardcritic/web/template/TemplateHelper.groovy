package com.cardboardcritic.web.template

import com.cardboardcritic.web.template.data.TemplateData

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class TemplateHelper {
    TemplateGlobals globals = new TemplateGlobals(baseUrl: 'http://localhost:8080/', siteTitle: 'CBC')

    def withGlobals(TemplateData templateData) {
        templateData.setGlobals globals
    }
}
