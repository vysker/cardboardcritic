package com.cardboardcritic.web.template.form;

import org.jboss.resteasy.annotations.jaxrs.FormParam;

public class GameEditForm {
    public int id;
    @FormParam public String name;
    @FormParam public String shortDescription;
    @FormParam public String description;
    @FormParam public String designer;
    @FormParam public String newDesigner;
    @FormParam public String slug;
    @FormParam public String releaseDate;
}
