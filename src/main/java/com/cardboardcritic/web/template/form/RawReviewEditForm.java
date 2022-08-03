package com.cardboardcritic.web.template.form;

import org.jboss.resteasy.annotations.jaxrs.FormParam;

public class RawReviewEditForm {
    public long id;
    @FormParam public String game;
    @FormParam public String newGame;
    @FormParam public String critic;
    @FormParam public String newCritic;
    @FormParam public String outlet;
    @FormParam public String newOutlet;
    @FormParam public String summary;
    @FormParam public String url;
    @FormParam public String title;
    @FormParam public String content;
    @FormParam public String date;
    @FormParam public int score;
    @FormParam public boolean recommended;
}
