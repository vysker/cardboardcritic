package com.cardboardcritic.feed.ars

import com.cardboardcritic.domain.RawReview
import com.cardboardcritic.feed.ArticleScraper
import groovy.json.JsonSlurper
import org.jsoup.nodes.Document

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
//articleUrl = 'https://arstechnica.com/gaming/2017/04/gloomhaven-review-2017s-biggest-board-game-is-astoundingly-good'
//articleUrl = 'https://arstechnica.com/gaming/2020/06/black-angel-review-run-your-own-tabletop-generation-ship/'
//def htmlRaw = new URL(articleUrl).text
//new File('ars.html') << htmlRaw
//def htmlFile = new File('p3.html')
//def document = Jsoup.parse(htmlFile, 'UTF-8')
//def document = visit(articleUrl)

class ArsArticleScraper extends ArticleScraper {

    @Override
    RawReview getReview(String articleUrl, Document document) {
        def finalPageUrl = getFinalPageUrl(document)
        if (finalPageUrl != null && finalPageUrl != articleUrl)
            document = fetch(finalPageUrl)

        def articleBody = document.select('div[itemprop="articleBody"]').last()
        def articleContentRaw = articleBody.getElementsByTag('p')
        def articleContent = articleContentRaw*.text()
        articleContent.removeIf { it.isEmpty() }

        def pageMeta = document.select('meta[name="parsely-page"]').first()
        def metaContent = pageMeta?.attr('content')
        def meta = new JsonSlurper().parseText(metaContent ?: '{}')

        new RawReview(
            title: meta.title,
            date: meta.pub_date ? LocalDateTime.parse(meta.pub_date, DateTimeFormatter.ISO_DATE_TIME) : null,
            critic: meta.author,
            paragraphs: articleContent,
            suggestedSummaries: articleContent?.takeRight(3) as List<String>,
            url: articleUrl
        )
    }

    String getFinalPageUrl(Document document) {
        def pageNumbers = document.select('nav.page-numbers span a')
        if (!pageNumbers)
            return null

        // if there is no arrow button saying 'next page', then we are already on the last page
        def isFinalPage = !pageNumbers.last().text().contains('Next')
        if (isFinalPage)
            return null

        // otherwise, take the item before the 'next' arrow
        def finalPage = pageNumbers.get(pageNumbers.size() - 2)
        return finalPage?.attr('href')
    }
}
