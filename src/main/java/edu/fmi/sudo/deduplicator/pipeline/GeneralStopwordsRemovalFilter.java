package edu.fmi.sudo.deduplicator.pipeline;

import edu.fmi.sudo.deduplicator.entities.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GeneralStopwordsRemovalFilter {
    private static final String stopwordsFile = "pipeline/stopwords.txt";
    private static final Set<String> stopWords = new HashSet<>();

    public static QuestionAnswers process(QuestionAnswers qa) {
        OriginalQuestion oq = qa.getQuestion();
        oq.setBody(removeStopwords(oq.getBody()));
        oq.setSubject(removeStopwords(oq.getSubject()));

        for(RelatedQuestion question: qa.getAllRelatedQuestions()) {
            question.setSubject(removeStopwords(question.getSubject()));
            question.setBody(removeStopwords(question.getBody()));
        }

        for(RelatedAnswer answer: qa.getAllRelatedAnswers()) {
            answer.setText(removeStopwords(answer.getText()));

            for(RelatedComment comment: answer.getRelatedComments()) {
                comment.setText(removeStopwords(comment.getText()));
            }
        }

        for(RelatedComment comment: qa.getAllRelatedComments()) {
            comment.setText(removeStopwords(comment.getText()));
        }

        return qa;
    }

    private static String removeStopwords(String text) {
        // add a dummy word to the end of the text in order to avoid the stream filer operation
        // returning an empty result in case the text consists entirely of stop words
        text = (text == null? "": text) + " dummy";

        readStopWords();

        String[] words = text.split(" ");
        Optional<String> reduced = Arrays.stream(words)
                .filter(x -> !stopWords.contains(x))
                .reduce((x, y) -> x + " " + y);

        if(reduced.isPresent())
            return reduced.get();

        return "";
    }

    private static void readStopWords() {
        if (GeneralStopwordsRemovalFilter.stopWords.size() > 0) return;

        InputStream stopWordsStream = GeneralStopwordsRemovalFilter.class.getClassLoader()
                .getResourceAsStream(stopwordsFile);

        InputStreamReader stopWordsInputReader = new InputStreamReader(stopWordsStream);
        BufferedReader stopWordsReader = new BufferedReader(stopWordsInputReader);

        String stopWord;
        try {
            while ((stopWord = stopWordsReader.readLine()) != null) {
                GeneralStopwordsRemovalFilter.stopWords.add(stopWord);
            }
            stopWordsReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
