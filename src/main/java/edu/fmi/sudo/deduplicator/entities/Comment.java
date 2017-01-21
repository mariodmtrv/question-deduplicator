package edu.fmi.sudo.deduplicator.entities;

import java.util.List;

/**
 * @author Miroslav Kramolinski
 */
public interface Comment {
    String getId();
    String getRelatedQuestionId();
    Relevance getRelevanceToRelated();
    String getText();
    List<String> getTextTokens();
}
