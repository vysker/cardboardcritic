package com.cardboardcritic.feed.ars

import com.cardboardcritic.db.entity.Outlet
import com.cardboardcritic.domain.RawReview
import groovy.json.JsonGenerator
import groovy.xml.XmlSlurper

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

def feedUrl = 'http://feeds.arstechnica.com/arstechnica/cardboard.rss'

//def rssRaw = new URL(feedUrl).text
//new File('ars.rss') << rssRaw

def rssRaw = new File('ars.xml').text

def jsonGenerator = new JsonGenerator.Options()
        .addConverter(LocalDateTime) { it.format(DateTimeFormatter.ISO_DATE_TIME) }
        .build()
def xmlParser = new XmlSlurper()
def rss = xmlParser.parseText(rssRaw)

List<String> links = rss.channel.item.link*.text()
println links.join(', ')

def today = LocalDate.now().format('yyyy-MM-dd')
def ars = new ArsArticleScraper()
def outlet = new Outlet(id: 1, name: 'Ars')

links.eachWithIndex { link, index ->
    RawReview review = ars.getReview(link)
    review.outlet = outlet

    def json = jsonGenerator.toJson(review)
    new File("reviews/ars_${today}_${index}.json") << json
}
