package edu.fmi.sudo.deduplicator.pipeline;

import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Lexicon;
import de.tudarmstadt.ukp.lmf.model.core.Sense;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;
import de.tudarmstadt.ukp.lmf.transform.DBConfig;
import edu.fmi.sudo.deduplicator.Logger;
import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;

import java.util.ArrayList;
import java.util.List;

/**
 * Applies word sense disambiguation by enriching the text with the the synonyms of the longer words or
 * the ones that start with a capital letter
 */
public class WSDFilter {
    public static final String UBY_DB_URL =  "jdbc:h2:file:embeddedUby/ubymedium070";
    public static final String UBY_DB_DRIVER = "org.h2.Driver";
    public static final String UBY_DB_DRIVER_NAME = "h2";
    public static final String UBY_DB_USERNAME = "sa";
    public static final String UBY_DB_PASSWORD = "";

    private static final Integer MAX_SYNONYMS = 5; // max number of synonyms to take for a word
    private static final Integer MIN_WORD_LENGTH = 4; // the min length of the words suitable for disambiguation

    public static QuestionAnswers process(QuestionAnswers qa) {
        DBConfig dbConfig = new DBConfig(
                UBY_DB_URL, UBY_DB_DRIVER,
                UBY_DB_DRIVER_NAME, UBY_DB_USERNAME,
                UBY_DB_PASSWORD, false);

        Uby uby = new Uby(dbConfig);
        if(uby.getSession() != null) {
            String preferredLexicon = "WordNet";
            Lexicon wordNet = uby.getLexiconByName(preferredLexicon);
            if(wordNet == null) {
                Logger.log("ERROR: Unable to locate the " + preferredLexicon + " lexicon from the UBY database. Skiping WSD filtering");
                return qa;
            }

            qa.getQuestion().setBody(enrichText(uby, wordNet, qa.getQuestion().getBody()));
            qa.getAllRelatedQuestions().stream().forEach(relQ -> relQ.setBody(enrichText(uby, wordNet, relQ.getBody())));
        }
        else {
            Logger.log("ERROR: Unable to obtain session from the underlying UBY database. Skipping WSD filtering");
            return qa;
        }

        return qa;
    }

    private static String enrichText(Uby uby, Lexicon lex, String text) {
        String origText = text;
        List<String> newText = new ArrayList<>();
        for(String word: text.split(" ")) {
            newText.add(word);

            if(word != null && ((word.charAt(0) >= 'A' && word.charAt(0) <= 'Z' && word.length() > 1) || word.length() >= MIN_WORD_LENGTH)) {
                List<String> synonyms = getSynonyms(uby, lex, word);
                newText.addAll(synonyms.size() <= MAX_SYNONYMS ? synonyms : synonyms.subList(0, MAX_SYNONYMS));
            }
        }

        return newText.stream().reduce((x, y) -> x + " " + y).get();
    }

    private static List<String> getSynonyms(Uby uby, Lexicon lex, String word) {
        List<LexicalEntry> lexEntries = uby.getLexicalEntries(word, lex);
        List<String> synonyms = new ArrayList<>();
        for(LexicalEntry lexEntry: lexEntries) {
            for(Sense sense: lexEntry.getSenses()) {
                Synset synset = sense.getSynset();
                if (synset != null) {
                    for (Sense s : synset.getSenses()) {
                        if(!synonyms.contains(s.getLexicalEntry().getLemmaForm()))
                            synonyms.add(s.getLexicalEntry().getLemmaForm());
                    }
                }
            }
        }

        return synonyms;
    }
}
