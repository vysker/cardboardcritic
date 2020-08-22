package com.cardboardcritic.web.template.data

import groovy.transform.ToString
import org.jboss.resteasy.annotations.jaxrs.FormParam

@ToString(includeNames = true)
class RawReviewEditData extends TemplateData {
    @FormParam String url, title, game, critic, outlet, summary, content, date
    @FormParam int score
    @FormParam boolean recommended
}
