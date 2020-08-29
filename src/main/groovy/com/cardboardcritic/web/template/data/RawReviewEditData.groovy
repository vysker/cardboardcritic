package com.cardboardcritic.web.template.data

import groovy.transform.ToString
import org.jboss.resteasy.annotations.jaxrs.FormParam

@ToString(includeNames = true)
class RawReviewEditData {
    @FormParam long id
    @FormParam String game, critic, outlet, summary, url, title, content, date
    @FormParam int score
    @FormParam boolean recommended
}
