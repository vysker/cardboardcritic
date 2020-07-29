package com.cardboardcritic.db.repository

import com.cardboardcritic.db.DbConfig
import com.cardboardcritic.db.entity.Game
import groovy.sql.Sql
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate

class GameRepositoryTest extends Specification {
    @Shared DbConfig dbConfig = new DbConfig(url: 'jdbc:h2:mem:', driver: 'org.h2.Driver')
    @Shared Sql sql = dbConfig.connect()
    @Shared String schema = this.class.getResource('/schema.sql').text

    GameRepository repo

    def setup() {
        repo = new GameRepository(sql)
        sql.execute schema
    }

    def cleanup() {
        sql.execute 'drop all objects'
        sql.close()
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

        when:
        def createdGameInDb = repo.create(game)
        def allGames = repo.index()
        def gameInDb = repo.find 1
        def isDeleted = repo.delete 1
        def allGamesAfterDelete = repo.index()

        then:
        createdGameInDb == game
        allGames == [game]
        gameInDb == game
        isDeleted
        allGamesAfterDelete == allGames - game
    }
}
