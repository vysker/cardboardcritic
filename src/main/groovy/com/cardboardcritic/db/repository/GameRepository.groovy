package com.cardboardcritic.db.repository


import com.cardboardcritic.db.DbConfig
import com.cardboardcritic.db.entity.Game

class GameRepository extends Repository<Game> {
    {
        table = 'games'
        type = Game
    }

    GameRepository(DbConfig config) {
        super(config)
    }

    @Override
    Game entityInstance() {
        new Game()
    }
}
