package com.cardboardcritic.db.repository

import com.cardboardcritic.db.DbConfig
import com.cardboardcritic.db.entity.Game
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate

class GameRepositoryTest extends Specification {
    @Shared DbConfig dbConfig = new DbConfig(url: 'jdbc:h2:mem:', driver: 'org.h2.Driver')
    @Shared String schema = this.class.getResource('/schema.sql').text

    GameRepository repo

    def setup() {
        repo = new GameRepository(dbConfig)
        repo.sql.execute schema
    }

    def cleanup() {
        repo.sql.execute 'drop all objects'
        repo.sql.close()
    }

    def 'Some simple ORM specs'() {
        given:

        def game = new Game(
                id: 1, score: 80, recommended: 70,
                name: 'Anachrony',
                shortDescription: 'Quite short',
                description: 'Bit longer',
                designer: 'David Turczi',
                releaseDate: LocalDate.now())
        repo.delete(1)

        when:
        def gameInDb = repo.create(game)
        def allGames = repo.index()
        def isDeleted = repo.delete(1)
        def allGamesAfterDelete = repo.index()

        then:
        gameInDb == game
        allGames == [game]
        isDeleted
        allGamesAfterDelete == allGames - game
    }
}
