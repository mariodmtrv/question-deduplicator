package edu.fmi.sudo.deduplicator.pipeline;

import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TokenizationFilter {
    public static QuestionAnswers process(QuestionAnswers qa) {
        QuestionAnswers result = qa;
        List<String> origQuestionTokens = tokenizeEntity(qa.getQuestion().getSubject());
        origQuestionTokens.addAll(tokenizeEntity(qa.getQuestion().getBody()));
        result.getQuestion().setTokens(origQuestionTokens);
            result.getAllRelatedAnswers().forEach(relatedAnswer -> {
                relatedAnswer.setTextTokens(tokenizeEntity(relatedAnswer.getText()));
            });
        result.getAllRelatedQuestions().forEach(relatedQuestion -> {
            List<String> relQuestionTokens = tokenizeEntity(relatedQuestion.getSubject());
            relQuestionTokens.addAll(tokenizeEntity(relatedQuestion.getBody()));
            relatedQuestion.setTokens(relQuestionTokens);
        });

            result.getAllRelatedComments().forEach(relatedComment -> {
                relatedComment.setTextTokens(tokenizeEntity(relatedComment.getText()));
            });
            result.getAllRelatedAnswers().forEach(relatedAnswer -> {
                relatedAnswer.getRelatedComments().forEach(relatedComment -> {
                    relatedComment.setTextTokens(tokenizeEntity(relatedComment.getText()));
                });
            });
        return result;
    }

    public static List<String> tokenizeEntity(String entity) {
        StringTokenizer st = new StringTokenizer(entity, " \t\n\r\f,.:;?![]'");
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }
        return tokens;
    }
}
