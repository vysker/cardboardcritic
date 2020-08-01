package com.cardboardcritic.gui.review

import com.cardboardcritic.db.entity.Critic
import com.cardboardcritic.db.entity.Game
import com.cardboardcritic.domain.Review
import groovy.json.JsonGenerator
import groovy.swing.SwingBuilder
import org.codehaus.groovy.runtime.InvokerHelper

import javax.swing.*
import javax.swing.SwingConstants as SC
import javax.swing.WindowConstants as WC
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.Document
import java.awt.*
import java.awt.event.ActionEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.List

class ReviewEditor {
    private final int MIN_SIZE = Short.MIN_VALUE
    private final int MAX_SIZE = Short.MAX_VALUE
    private final int FRAME_WIDTH = 600
    private final int FRAME_HEIGHT = 768
    private final int PADDING = 10

    // GUI defaults
    // Note: it appears that 'alignmentX: SwingConstants.LEFT' is for containers, while 'alignmentX: 0' is for components
    private final def alignLeft = [alignmentX: 0]
    private final def defaultBorder = BorderFactory.createLineBorder(Color.gray)
    private final def inputFieldPaddingBorder = BorderFactory.createEmptyBorder(2, 5, 2, 5)
    private final def inputFieldBorder = BorderFactory.createCompoundBorder(defaultBorder, inputFieldPaddingBorder)
    private final def formRowSize = new Dimension(FRAME_WIDTH, 40)
    private final def formRowSizes = [minimumSize: formRowSize, maximumSize: formRowSize, preferredSize: formRowSize]
    private final def formRowDefaults = [*: formRowSizes, alignmentX: SC.LEFT] // container
    private final def inputFieldDefaults = [*: alignLeft, border: inputFieldBorder] // input field itself

    // GUI item refs
    private JComboBox gameSelect

    private final ReviewModel model
    private final JsonGenerator jsonGenerator
    private final List<Game> games
    private final List<Critic> critics

    ReviewEditor(Review review, List<Game> games, List<Critic> critics) {
        this.critics = critics
        this.games = games
        this.model = new ReviewModel()
        copyProperties(review, model)
//        model.gameSearchMatches = 0..games.size()

        jsonGenerator = new JsonGenerator.Options()
                .addConverter(LocalDateTime) { it.format(DateTimeFormatter.ISO_DATE_TIME) }
                .build()
    }

    def start() {
        new SwingBuilder().edt {
            frame(title: 'Scrape',
                    size: [FRAME_WIDTH, FRAME_HEIGHT],
                    defaultCloseOperation: WC.EXIT_ON_CLOSE,
                    locationRelativeTo: null, // center frame on screen
                    pack: true,
                    show: true
            ) {
                // serves as horizontal/vertical margin between components
                def vfiller = { rigidArea size: new Dimension(MIN_SIZE, PADDING) }
                def hfiller = { rigidArea size: new Dimension(PADDING, MIN_SIZE) }

                vbox(border: BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)) {
                    vbox(*: formRowDefaults) {
                        label text: 'URL'
                        textField text: model.url, *: inputFieldDefaults
                    }
                    vfiller()
                    hbox(*: formRowDefaults) {
                        vbox() {
                            label text: 'Game'
                            def searchField = textField text: model.game, *: inputFieldDefaults
                            searchField.document.addDocumentListener new DefaultDocumentListener({ searchGame it })
                        }
                        hfiller()
                        vbox() {
                            gameSelect = comboBox items: games.indices
                            gameSelect.renderer = new GameSelectRenderer()
                        }
                    }
                    vfiller()
                    hbox(*: formRowDefaults) {
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
                    hbox(*: formRowDefaults) {
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
                                    hbox(*: alignLeft) {
                                        scrollPane() {
                                            editorPane text: model.suggestedSummaries[index], border: inputFieldPaddingBorder
                                        }
                                        radioButton buttonGroup: group, actionCommand: index, actionListener: { chooseSummary() }
                                    }
                                    vfiller()
                                }
                            }
                        }
                    }
                    vfiller()
                    vbox(*: formRowDefaults, preferredSize: new Dimension(FRAME_WIDTH, 80)) {
                        label text: 'All Paragraphs'
                        scrollPane(*: alignLeft) {
                            editorPane text: model.paragraphs.join('\n\n'), border: inputFieldPaddingBorder
                        }
                    }
                    vfiller()
                    vbox(*: formRowDefaults) {
                        button text: 'Save', *: formRowSizes, actionPerformed: { saveAction() }
                    }
                }
            }
        }
    }

    def saveAction() {
        def summary = model.suggestedSummaries[model.chosenSummary]
        def game = games[gameSelect.selectedIndex].name

        def er = new EditedReview()
        copyProperties(model, er)
        er.summary = summary
        er.game = game

        def json = jsonGenerator.toJson(er)
        println json
//        new File("${er.game}_${er.author}") << json
    }

    def chooseSummary(ActionEvent e) {
        model.chosenSummary = e.getActionCommand().toInteger()
    }

    def searchGame(String query) {
        query = query.strip()
        gameSelect.removeAllItems()

        def matches

        if (query) matches = games.findIndexValues { it.name.containsIgnoreCase query }
        else matches = games.indices

        matches.each {gameSelect.addItem it }
    }

    def copyProperties(Object source, Object target) {
        use(InvokerHelper) {
            target.properties = source.properties
        }
    }

    private class GameSelectRenderer extends DefaultListCellRenderer {
        @Override
        Component getListCellRendererComponent(JList jList, Object value, int index, boolean selected, boolean focused) {
            super.getListCellRendererComponent jList, value, index, selected, focused
            if (value != null) text = games[value as int]?.name
            this
        }
    }

    private class DefaultDocumentListener implements DocumentListener {
        private final Closure action

        DefaultDocumentListener(Closure action) {
            this.action = action
        }

        def update(DocumentEvent event) {
            Document doc = event.document
            String text = doc.getText doc.startPosition.offset, doc.endPosition.offset
            action.call text
        }

        @Override
        void insertUpdate(DocumentEvent event) { update event }

        @Override
        void removeUpdate(DocumentEvent event) { update event }

        @Override
        void changedUpdate(DocumentEvent event) { update event }
    }
}
