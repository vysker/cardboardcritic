package com.cardboardcritic.web.template.data;

import com.cardboardcritic.db.entity.Game;

import java.util.List;

public record RecentData(List<Game> games) {
}
