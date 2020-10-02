package com.cardboardcritic.web.template.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.jboss.resteasy.annotations.jaxrs.FormParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
public class RawReviewEditData {
    @FormParam private long id;
    @FormParam private String game;
    @FormParam private String critic;
    @FormParam private String outlet;
    @FormParam private String summary;
    @FormParam private String url;
    @FormParam private String title;
    @FormParam private String content;
    @FormParam private String date;
    @FormParam private int score;
    @FormParam private boolean recommended;
}
