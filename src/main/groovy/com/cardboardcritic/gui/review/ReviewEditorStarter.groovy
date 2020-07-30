package com.cardboardcritic.gui.review

import com.cardboardcritic.domain.Review
import groovy.json.JsonSlurper

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

def reviewRaw = this.class.getResource('/ars-review.json').text
def reviewJson = new JsonSlurper().parseText(reviewRaw) as Map
reviewJson.date = LocalDateTime.parse ( reviewJson.date as String, DateTimeFormatter.ISO_DATE_TIME )
def review = new Review(reviewJson)

def editor = new ReviewEditor(review)
editor.start()
