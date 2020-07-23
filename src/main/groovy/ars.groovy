import groovy.json.JsonGenerator
import groovy.json.JsonSlurper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

srcUrl = 'https://arstechnica.com/gaming/2017/04/gloomhaven-review-2017s-biggest-board-game-is-astoundingly-good'
//srcUrl = 'https://arstechnica.com/gaming/2020/06/black-angel-review-run-your-own-tabletop-generation-ship/'

//def htmlRaw = new URL(srcUrl).text
//new File('ars.html') << htmlRaw

def htmlFile = new File('p3.html')
def document = Jsoup.parse(htmlFile, 'UTF-8')

//def document = visit(srcUrl)
def review = getReview(document)
def jsonGenerator = new JsonGenerator.Options()
    .addConverter(LocalDateTime) { it.format(DateTimeFormatter.ISO_DATE_TIME) }
    .build()
def json = jsonGenerator.toJson(review)
new File('ars-review.json') << json

Review getReview(Document document) {
    def finalPageUrl = getFinalPageUrl(document)
    if (finalPageUrl != null && finalPageUrl != srcUrl)
        document = visit(finalPageUrl)

    def articleBody = document.select('div[itemprop="articleBody"]').last()
    def articleContentRaw = articleBody.getElementsByTag('p')
    def articleContent = articleContentRaw*.text()
    articleContent.removeIf { it.isEmpty() }

    def pageMeta = document.select('meta[name="parsely-page"]').first()
    def metaContent = pageMeta?.attr('content')
    def meta = new JsonSlurper().parseText(metaContent ?: '{}')

    return new Review().with {
        title = meta.title
        date = meta.pub_date ? LocalDateTime.parse(meta.pub_date, DateTimeFormatter.ISO_DATE_TIME) : null
        author = meta.author
        paragraphs = articleContent
        suggestedSummaries = articleContent?.takeRight(3) as List<String>
        url = srcUrl
        it
    }
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

Document visit(String url) {
    println "Visiting: $url"
    return Jsoup.connect(url).get()
}
