package com.cardboardcritic.domain

import com.cardboardcritic.db.entity.Critic
import com.cardboardcritic.db.entity.Game
import com.cardboardcritic.db.entity.Outlet

import java.time.LocalDateTime

class EditedReview {
    String link, summary
    LocalDateTime date
    Critic critic
    Outlet outlet
    Game game
    int score
    boolean recommended
}
