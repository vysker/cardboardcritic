package com.cardboardcritic.data;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.web.template.form.ReviewEditForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface ReviewMapper {
    @Mapping(source = "game.name", target = "game")
    @Mapping(source = "critic.name", target = "critic")
    @Mapping(source = "outlet.name", target = "outlet")
    @Mapping(source = "summary", target = "content")
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "processed", ignore = true)
    RawReview toRawReview(Review review);

    @Mapping(source = "game.name", target = "game")
    @Mapping(source = "critic.name", target = "critic")
    @Mapping(source = "outlet.name", target = "outlet")
    ReviewEditForm toForm(Review review);

    @Mapping(source = "game", target = "game.name")
    @Mapping(source = "critic", target = "critic.name")
    @Mapping(source = "outlet", target = "outlet.name")
    Review fromForm(ReviewEditForm review);
}
