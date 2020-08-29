package com.cardboardcritic.web.template.data

import com.cardboardcritic.db.entity.Game
import groovy.transform.ToString

@ToString(includeNames = true)
class RecentData {
    List<Game> games
}
