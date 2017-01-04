package edu.fmi.sudo.deduplicator.entities;

import java.util.Date;

/**
 * @author Miroslav Kramolinski
 */
public interface Related {
    Date getDate();
    String getOriginalQuestionId();
    Relevance getRelevanceToOriginalQuestion();
    int getScore();
    int getUserId();
    String getUsername();
}
