package com.cardboardcritic.gui.review

import com.cardboardcritic.db.DbConfig
import com.cardboardcritic.db.entity.Critic
import com.cardboardcritic.db.entity.Game
import com.cardboardcritic.db.entity.Outlet
import com.cardboardcritic.db.entity.Review
import com.cardboardcritic.db.repository.CriticRepository
import com.cardboardcritic.db.repository.GameRepository
import com.cardboardcritic.db.repository.OutletRepository
import com.cardboardcritic.db.repository.ReviewRepository
import com.cardboardcritic.domain.EditedReview
import com.cardboardcritic.domain.RawReview
import groovy.json.JsonSlurper
import groovy.sql.Sql

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

def games = [new Game(id: 1, name: 'Gloomhaven'), new Game(id: 2, name: 'Anachrony')]
def critics = [new Critic(id: 1, name: 'John Doe'), new Critic(id: 2, name: 'Jane Kane')]
def outlets = [new Outlet(id: 1, name: 'Ars Technica'), new Outlet(id: 2, name: 'Eurogamer')]

DbConfig dbConfig = new DbConfig(url: 'jdbc:h2:mem:', driver: 'org.h2.Driver')
Sql sql = dbConfig.connect()
String schema = this.class.getResource('/schema.sql').text
sql.execute schema

def gameRepo = new GameRepository()
def criticRepo = new CriticRepository()
def outletRepo = new OutletRepository()
def reviewRepo = new ReviewRepository()
games.each { gameRepo.persist it }
critics.each { criticRepo.persist it }
outlets.each { outletRepo.persist it }

def reviewRaw = this.class.getResource('/ars-review.json').text
def reviewJson = new JsonSlurper().parseText(reviewRaw) as Map
reviewJson.date = LocalDateTime.parse(reviewJson.date as String, DateTimeFormatter.ISO_DATE_TIME)
def review = new RawReview(reviewJson)
review.outlet = outletRepo.findById review.outlet.id

def onSave = { EditedReview editedReview ->
    def r = new Review(
            gameId: editedReview.game.id,
            criticId: editedReview.critic.id,
            outletId: editedReview.outlet.id,
            score: editedReview.score,
            summary: editedReview.summary,
            link: editedReview.link,
            recommended: editedReview.recommended
    )
    println "r: $r"
    reviewRepo.persist r

//    def jsonGenerator = new JsonGenerator.Options()
//            .addConverter(LocalDateTime) { it.format(DateTimeFormatter.ISO_DATE_TIME) }
//            .build()
//    def json = jsonGenerator.toJson r
//    println json
//    new File("${er.game}_${er.critic}") << json
}

def editor = new ReviewEditor(review, sql, onSave)
editor.start()


