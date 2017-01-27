package edu.fmi.sudo.deduplicator.pipeline;

import edu.fmi.sudo.deduplicator.entities.*;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Miroslav Kramolinski
 */
public class HtmlUnescapeFilter {
    public static QuestionAnswers process(QuestionAnswers qa) {
        OriginalQuestion oq = qa.getQuestion();
        oq.setBody(unescape(oq.getBody()));
        oq.setSubject(unescape(oq.getSubject()));

        for(RelatedQuestion question: qa.getAllRelatedQuestions()) {
            question.setSubject(unescape(question.getSubject()));
            question.setBody(unescape(question.getBody()));
        }

        for(RelatedAnswer answer: qa.getAllRelatedAnswers()) {
            answer.setText(unescape(answer.getText()));

            for(RelatedComment comment: answer.getRelatedComments()) {
                comment.setText(unescape(comment.getText()));
            }
        }

        for(RelatedComment comment: qa.getAllRelatedComments()) {
            comment.setText(unescape(comment.getText()));
        }

        return qa;
    }

    private static String unescape(String text) {
        Optional<String> reduced = Arrays.stream(text.split(" "))
                                        .map(x -> StringEscapeUtils.unescapeHtml(x))
                                        .reduce((x, y) -> x + " " + y);

        if(reduced.isPresent())
            return reduced.get();

        return "";
    }
}
