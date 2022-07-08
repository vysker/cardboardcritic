package com.cardboardcritic.web.template.form;

import org.jboss.resteasy.reactive.RestForm;

public class RawReviewEditForm {
    public long id;
    @RestForm public String game;
    @RestForm public String critic;
    @RestForm public String outlet;
    @RestForm public String summary;
    @RestForm public String url;
    @RestForm public String title;
    @RestForm public String content;
    @RestForm public String date;
    @RestForm public int score;
    @RestForm public boolean recommended;
}
