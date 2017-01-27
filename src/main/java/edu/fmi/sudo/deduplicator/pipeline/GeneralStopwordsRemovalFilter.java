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
        for(int i = 0; i < words.length; i ++) {
            if(words[i] == null || words[i].isEmpty())
                continue;

            if(stopWords.contains(words[i]))
                words[i] = "";
            else {
                for(String stopWord: stopWords) {
                    // Remove the prefix/suffix stop words from the main word (like exclamation mark)
                    if(stopWord != null && stopWord.toLowerCase().charAt(0) < 'a' && stopWord.toLowerCase().charAt(0) > 'z') {
                        if(words[i].startsWith(stopWord))
                            words[i] = words[i].substring(stopWord.length());
                        else if(words[i].endsWith(stopWord))
                            words[i] = words[i].substring(0, words[i].length() - stopWord.length());
                    }

                }
            }
        }

        Optional<String> reduced = Arrays.stream(words)
                                        .filter(x -> x != null && !x.isEmpty())
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
