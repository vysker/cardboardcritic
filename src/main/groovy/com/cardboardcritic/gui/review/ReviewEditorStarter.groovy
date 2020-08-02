package com.cardboardcritic.gui.review

import com.cardboardcritic.db.DbConfig
import com.cardboardcritic.db.entity.Critic
import com.cardboardcritic.db.entity.Game
import com.cardboardcritic.db.entity.Outlet
import com.cardboardcritic.db.repository.CriticRepository
import com.cardboardcritic.db.repository.GameRepository
import com.cardboardcritic.db.repository.OutletRepository
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

def gameRepo = new GameRepository(sql)
def criticRepo = new CriticRepository(sql)
def outletRepo = new OutletRepository(sql)
games.each { gameRepo.create it }
critics.each { criticRepo.create it }
outlets.each { outletRepo.create it }

def reviewRaw = this.class.getResource('/ars-review.json').text
def reviewJson = new JsonSlurper().parseText(reviewRaw) as Map
reviewJson.date = LocalDateTime.parse(reviewJson.date as String, DateTimeFormatter.ISO_DATE_TIME)
def review = new RawReview(reviewJson)
review.outlet = outletRepo.find review.outlet.id

def editor = new ReviewEditor(review, sql)
editor.start()
