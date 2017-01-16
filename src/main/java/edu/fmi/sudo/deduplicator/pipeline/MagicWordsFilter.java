package edu.fmi.sudo.deduplicator.pipeline;

import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;

/**
 * Locate words from the subjects/bodies of comments/questions that could denote that they are a duplicate of the original
 * Try to find a match with the pre-defined magic words, as long as the collocation is not preceded by a negation
 */
public class MagicWordsFilter {
    private final String filename = "resources/pipeline/magicwords.txt";

    public static QuestionAnswers process(QuestionAnswers qa) {
        // TODO
        return null;
    }
}
