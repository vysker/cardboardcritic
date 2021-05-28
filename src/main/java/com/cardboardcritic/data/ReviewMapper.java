package com.cardboardcritic.data;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface ReviewMapper {
    @Mapping(source = "game.name", target = "game")
    @Mapping(source = "critic.name", target = "critic")
    @Mapping(source = "outlet.name", target = "outlet")
    RawReview toRawReview(Review review);
}
