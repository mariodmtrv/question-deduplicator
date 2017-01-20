package edu.fmi.sudo.deduplicator.pipeline;

import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TokenizationFilter {
    public static QuestionAnswers process(QuestionAnswers qa) {
        // TODO
        // MAYBE Adds new fields to QuestionAnswers entities that include tokenized versions
        //of them Should subclass?
        return qa;
    }

    public static List<String> tokenizeEntity(String entity) {
        StringTokenizer st = new StringTokenizer(entity);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }
        return tokens;
    }
}
