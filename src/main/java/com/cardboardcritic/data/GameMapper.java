package com.cardboardcritic.data;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.web.template.form.GameEditForm;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta-cdi")
public interface GameMapper {
    GameEditForm toForm(Game data);
}
