package edu.fmi.sudo.deduplicator.pipeline;

import edu.fmi.sudo.deduplicator.entities.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneralStopwordsRemovalFilter {
    private static final String stopwordsFile = "src\\main\\resources\\pipeline\\stopwords.txt";

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

        try(Stream<String> stream = Files.lines(Paths.get(stopwordsFile))) {
            final Set<String> stopwords = stream.collect(Collectors.toSet());
            String[] words = text.split(" ");
            Optional<String> reduced = Arrays.stream(words)
                    .filter(x -> !stopwords.contains(x))
                    .reduce((x, y) -> x + " " + y);

            if(reduced.isPresent())
                return reduced.get();

            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
