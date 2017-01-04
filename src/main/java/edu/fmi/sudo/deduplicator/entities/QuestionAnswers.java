/**
 * Represents a question, related questions, related answers and comments
 *
 * @author Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.entities;

import java.util.List;

public class QuestionAnswers {
    private OriginalQuestion question;
    private Thread relatedThread;

    public QuestionAnswers(OriginalQuestion question, Thread thread) {
        this.question = question;
        relatedThread = thread;
    }

    public OriginalQuestion getQuestion() {
        return question;
    }

    public List<RelatedAnswer> getRelatedAnswer() {
        return relatedThread.getRelatedAnswers();
    }

    public RelatedComment getRelatedCommentById(String id) {
        return relatedThread.getRelatedCommentById(id);
    }

    public List<RelatedComment> getRelatedComments() {
        return relatedThread.getRelatedComments();
    }
}
