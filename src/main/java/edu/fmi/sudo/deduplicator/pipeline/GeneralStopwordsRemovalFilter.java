package edu.fmi.sudo.deduplicator.pipeline;

import edu.fmi.sudo.deduplicator.entities.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class GeneralStopwordsRemovalFilter {
    private static final String stopwordsFile = "resources/pipeline/stopwords.txt";

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

        return null;
    }

    private static String removeStopwords(String text) {
        Set<String> stopwords = new HashSet<>();

        try(Stream<String> stream = Files.lines(Paths.get(stopwordsFile))) {
            stream.forEach(x -> stopwords.add(x));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] words = text.split(" ");
        return Arrays.stream(words)
                .filter(x -> stopwords.contains(x))
                .reduce((x, y) -> x + " " + y)
                .get();
    }
}
