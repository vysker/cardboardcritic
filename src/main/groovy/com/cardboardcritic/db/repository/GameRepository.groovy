package com.cardboardcritic.db.repository

import com.cardboardcritic.db.entity.Game
import groovy.sql.Sql

class GameRepository extends Repository<Game> {
    {
        table = 'games'
        type = Game
    }

    GameRepository(Sql sql) {
        super(sql)
    }

    @Override
    Game entityInstance() {
        new Game()
    }
}
