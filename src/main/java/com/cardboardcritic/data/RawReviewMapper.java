package com.cardboardcritic.data;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.web.template.form.RawReviewEditForm;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface RawReviewMapper {
    RawReviewEditForm toTemplateData(RawReview data);
}
