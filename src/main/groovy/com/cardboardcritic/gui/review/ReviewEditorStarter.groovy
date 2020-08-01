package com.cardboardcritic.gui.review

import com.cardboardcritic.db.entity.Critic
import com.cardboardcritic.db.entity.Game
import com.cardboardcritic.domain.Review
import groovy.json.JsonSlurper

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

def reviewRaw = this.class.getResource('/ars-review.json').text
def reviewJson = new JsonSlurper().parseText(reviewRaw) as Map
reviewJson.date = LocalDateTime.parse ( reviewJson.date as String, DateTimeFormatter.ISO_DATE_TIME )
def review = new Review(reviewJson)

def games = [new Game(id: 1, name: 'Gloomhaven'), new Game(id: 2, name: 'Anachrony')]
def critics = [new Critic(id: 1, name: 'John Doe'), new Critic(id: 2, name: 'Jane Kane')]

def editor = new ReviewEditor(review, games, critics)
editor.start()
