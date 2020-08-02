package com.cardboardcritic.gui.review

import com.cardboardcritic.db.entity.Critic
import com.cardboardcritic.db.entity.Game
import com.cardboardcritic.db.repository.CriticRepository
import com.cardboardcritic.db.repository.GameRepository
import com.cardboardcritic.domain.EditedReview
import com.cardboardcritic.domain.HasName
import com.cardboardcritic.domain.RawReview
import groovy.json.JsonGenerator
import groovy.sql.Sql
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
import java.util.function.Consumer
import java.util.function.Supplier

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
    private final def formRowDefaults = [*: formRowSizes, opaque: true, background: Color.lightGray, alignmentX: SC.LEFT]
    // container
    private final def inputFieldDefaults = [*: alignLeft, border: inputFieldBorder] // input field itself
    private final def pct25 = [preferredSize: new Dimension((FRAME_WIDTH / 4).intValue(), MAX_SIZE)]
    private final def pct33 = [preferredSize: new Dimension((FRAME_WIDTH / 3).intValue(), MAX_SIZE)]
    private final def pct50 = [preferredSize: new Dimension((FRAME_WIDTH / 2).intValue(), MAX_SIZE)]
    private final def pct66 = [preferredSize: new Dimension((FRAME_WIDTH / 1.5).intValue(), MAX_SIZE)]
    private final def pct75 = [preferredSize: new Dimension((FRAME_WIDTH * 0.75).intValue(), MAX_SIZE)]

    // GUI item refs
    private JComboBox gameSelect
    private JComboBox criticSelect
    private JTextField criticSearchField

    private final ReviewModel model
    private final JsonGenerator jsonGenerator
    private final GameRepository gameRepo
    private final CriticRepository criticRepo
    private List<Game> games
    private List<Critic> critics

    ReviewEditor(RawReview review, Sql sql) {
        this.gameRepo = new GameRepository(sql)
        this.criticRepo = new CriticRepository(sql)
        this.games = gameRepo.index()
        this.critics = criticRepo.index()
        this.model = new ReviewModel()
        copyProperties(review, model)

        jsonGenerator = new JsonGenerator.Options()
                .addConverter(LocalDateTime) { it.format(DateTimeFormatter.ISO_DATE_TIME) }
                .build()
    }

    def start() {
        new SwingBuilder().edt {
            frame(title: 'Scrape',
                    size: new Dimension(FRAME_WIDTH, FRAME_HEIGHT),
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
                        vbox(*: pct50) {
                            label text: 'Game'
                            def searchField = textField text: model.game, *: inputFieldDefaults
                            searchField.document.addDocumentListener new DefaultDocumentListener(this::searchGame)
                        }
                        hfiller()
                        vbox(*: pct50) {
                            label text: 'Game search result'
                            gameSelect = comboBox items: games.indices, *: alignLeft
                            gameSelect.renderer = new IndexedSelectRenderer({ games })
                        }
                    }
                    vfiller()
                    hbox(*: formRowDefaults) {
                        vbox(*: pct33) {
                            label text: 'Critic'
                            criticSearchField = textField text: model.critic, *: inputFieldDefaults
                            criticSearchField.document.addDocumentListener new DefaultDocumentListener(this::searchCritic)
                        }
                        hfiller()
                        vbox(*: pct33) {
                            label text: 'Action'
                            button text: 'Create critic', actionPerformed: { createCritic criticSearchField.text }, *: alignLeft
                        }
                        hfiller()
                        vbox(*: pct33) {
                            label text: 'Critic search result'
                            criticSelect = comboBox items: critics.indices, *: alignLeft
                            criticSelect.renderer = new IndexedSelectRenderer({ critics })
                        }
                    }
                    vfiller()
                    hbox(*: formRowDefaults) {
                        vbox(*: pct50) {
                            label text: 'Review title'
                            label text: model.title, *: inputFieldDefaults
                        }
                        hfiller()
                        vbox(*: pct50) {
                            label text: 'Review publish date'
                            def input = textField text: model.date, *: inputFieldDefaults
                            def setDate = { model.date = input.text }
                            input.document.addDocumentListener new DefaultDocumentListener(setDate)
                        }
                    }
                    vfiller()
                    hbox(*: formRowDefaults) {
                        vbox(*: pct33) {
                            label text: 'Outlet'
                            label text: model.outlet.name, *: inputFieldDefaults
                        }
                        hfiller()
                        vbox(*: pct33) {
                            label text: 'Score'
                            def input = textField text: model.score, *: inputFieldDefaults
                            def setScore = { model.score = (input.text) ? input.text.toInteger() : 0 }
                            input.document.addDocumentListener new DefaultDocumentListener(setScore)
                        }
                        hfiller()
                        vbox(*: pct33) {
                            label text: 'Recommended'
                            def input = checkBox text: 'Recommended', selected: model.recommended, *: inputFieldDefaults
                            input.addItemListener { model.recommended = input.selected }
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
                                        radioButton buttonGroup: group, actionCommand: index, selected: true, actionListener: { chooseSummary() }
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
                        button text: 'Save', *: formRowSizes, actionPerformed: { save() }
                    }
                }
            }
        }
    }

    def save() {
        def summary = model.suggestedSummaries[model.chosenSummary]
        def game = games[gameSelect.selectedIndex]
        def critic = critics[criticSelect.selectedIndex]

        def er = new EditedReview(
                link: model.url,
                date: LocalDateTime.parse(model.date),
                outlet: model.outlet,
                score: model.score,
                recommended: model.recommended,
                summary: summary,
                game: game,
                critic: critic
        )

        def json = jsonGenerator.toJson(er)
        println json
//        new File("${er.game}_${er.critic}") << json
    }

    def chooseSummary(ActionEvent e) {
        model.chosenSummary = e.getActionCommand().toInteger()
    }

    def searchGame(String query) {
        searchByName query, games, gameSelect
    }

    def searchCritic(String query) {
        searchByName query, critics, criticSelect
    }

    def createCritic(String name) {
        name = name.strip()
        if (!name) return

        criticRepo.create new Critic(name: name)
        critics = criticRepo.index()
        searchCritic name
    }

    static def searchByName(String query, List<HasName> items, JComboBox selectBox) {
        query = query.strip()
        selectBox.removeAllItems()

        def matches

        if (query) matches = items.findIndexValues { it.name.containsIgnoreCase query }
        else matches = items.indices

        matches.each { selectBox.addItem it }
    }

    static def copyProperties(Object source, Object target) {
        use(InvokerHelper) {
            target.properties = source.properties
        }
    }

    private class IndexedSelectRenderer extends DefaultListCellRenderer {
        private final Supplier<List<HasName>> items

        IndexedSelectRenderer(Supplier<List<HasName>> items) {
            this.items = items
        }

        @Override
        Component getListCellRendererComponent(JList jList, Object value, int index, boolean selected, boolean focused) {
            super.getListCellRendererComponent jList, value, index, selected, focused
            if (value != null) text = items.get()[value as int]?.name
            this
        }
    }

    private class DefaultDocumentListener implements DocumentListener {
        private final Consumer<String> action

        DefaultDocumentListener(Consumer<String> action) {
            this.action = action
        }

        def update(DocumentEvent event) {
            Document doc = event.document
            String text = doc.getText doc.startPosition.offset, doc.endPosition.offset
            action.accept text
        }

        @Override
        void insertUpdate(DocumentEvent event) { update event }

        @Override
        void removeUpdate(DocumentEvent event) { update event }

        @Override
        void changedUpdate(DocumentEvent event) { update event }
    }
}
