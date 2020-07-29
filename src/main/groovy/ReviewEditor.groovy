import groovy.json.JsonGenerator
import groovy.json.JsonSlurper
import groovy.swing.SwingBuilder
import org.codehaus.groovy.runtime.InvokerHelper

import javax.swing.*
import javax.swing.SwingConstants as SC
import javax.swing.WindowConstants as WC
import java.awt.*
import java.awt.event.ActionEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

def MIN_SIZE = Short.MIN_VALUE
def MAX_SIZE = Short.MAX_VALUE
def FRAME_WIDTH = 600
def FRAME_HEIGHT = 768
def PADDING = 10

def jsonGenerator = new JsonGenerator.Options()
        .addConverter(LocalDateTime) { it.format(DateTimeFormatter.ISO_DATE_TIME) }
        .build()

def reviewRaw = new File('ars-review.json').text
def reviewJson = new JsonSlurper().parseText(reviewRaw) as Map
reviewJson.date = LocalDateTime.parse(reviewJson.date as String, DateTimeFormatter.ISO_DATE_TIME)

def review = new Review(reviewJson)
def model = new ReviewModel()
copyProperties(review, model)

// GUI defaults
// Note: it appears that 'alignmentX: SwingConstants.LEFT' is for containers, while 'alignmentX: 0' is for components.
def alignLeft = [alignmentX: 0]
def defaultBorder = BorderFactory.createLineBorder(Color.gray)
def inputFieldPaddingBorder = BorderFactory.createEmptyBorder(2, 5, 2, 5)
def inputFieldBorder = BorderFactory.createCompoundBorder(defaultBorder, inputFieldPaddingBorder)
def formRowSize = new Dimension(FRAME_WIDTH, 40)
def formRowSizes = [minimumSize: formRowSize, maximumSize: formRowSize, preferredSize: formRowSize]
def formRowDefaults = [*:formRowSizes, alignmentX: SC.LEFT] // container
def inputFieldDefaults = [*:alignLeft, border: inputFieldBorder] // input field itself

def saveAction = { ReviewModel rm ->
    def summary = rm.suggestedSummaries[rm.chosenSummary]

    def er = new EditedReview()
    copyProperties(rm, er)
    er.summary = summary

    def json = jsonGenerator.toJson(er)
    new File("${er.game}_${er.author}") << json
}

def chooseSummary = { ActionEvent e ->
    model.chosenSummary = e.getActionCommand().toInteger()
}

new SwingBuilder().edt {
    frame(title: 'Scrape',
            size: [FRAME_WIDTH, FRAME_HEIGHT],
            defaultCloseOperation: WC.EXIT_ON_CLOSE,
            locationRelativeTo: null, // center frame on screen
            pack: true,
            show: true
    ) {
        def vfiller = { rigidArea size: new Dimension(MIN_SIZE, PADDING) } // serves as vertical margin between components
        def hfiller = { rigidArea size: new Dimension(PADDING, MIN_SIZE) } // serves as horizontal margin between components

        vbox(border: BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)) {
            vbox(*: formRowDefaults) {
                label text: 'URL'
                textField text: model.url, *: inputFieldDefaults
            }
            vfiller()
            vbox(*: formRowDefaults) {
                label text: 'Game'
                textField text: model.game, *: inputFieldDefaults
            }
            vfiller()
            hbox(*:formRowDefaults) {
                vbox() {
                    label text: 'Outlet'
                    textField text: model.outlet, *: inputFieldDefaults
                }
                hfiller()
                vbox() {
                    label text: 'Author'
                    textField text: model.author, *: inputFieldDefaults
                }
            }
            vfiller()
            hbox(*:formRowDefaults) {
                vbox() {
                    label text: 'Review title'
                    textField text: model.title, *: inputFieldDefaults
                }
                hfiller()
                vbox() {
                    label text: 'Review publish date'
                    textField text: model.date, *: inputFieldDefaults
                }
            }
            vfiller()
            vbox(alignmentX: SC.LEFT, border: defaultBorder, preferredSize: new Dimension(FRAME_WIDTH, 400)) {
                label text: 'Suggested Summaries'
                vbox() {
                    buttonGroup().with { group ->
                        model.suggestedSummaries.eachWithIndex { summary, index ->
                            hbox(*:alignLeft) {
                                scrollPane() {
                                    editorPane text: model.suggestedSummaries[index], border: inputFieldPaddingBorder
                                }
                                radioButton buttonGroup: group, actionCommand: index, actionListener: chooseSummary
                            }
                            vfiller()
                        }
                    }
                }
            }
            vfiller()
            vbox(*:formRowDefaults, preferredSize: new Dimension(FRAME_WIDTH, 80)) {
                label text: 'All Paragraphs'
                scrollPane(*:alignLeft) {
                    editorPane text: model.paragraphs.join('\n\n'), border: inputFieldPaddingBorder
                }
            }
            vfiller()
            vbox(*:formRowDefaults) {
                button text: 'Save', *:formRowSizes, actionPerformed: { saveAction(model) }
            }
        }
    }
}

class EditedReview {
    String url, title, game, author, outlet, summary
    LocalDateTime date
}

class ReviewModel extends Review {
    int chosenSummary
}

def copyProperties(Object source, Object target) {
    use(InvokerHelper) {
        target.properties = source.properties
    }
}
