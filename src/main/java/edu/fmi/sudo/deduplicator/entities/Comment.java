package edu.fmi.sudo.deduplicator.entities;

/**
 * @author Miroslav Kramolinski
 */
public interface Comment {
    String getId();
    String getRelatedQuestionId();
    Relevance getRelevanceToRelated();
    String getText();
}
