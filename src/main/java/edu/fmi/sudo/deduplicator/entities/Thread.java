package edu.fmi.sudo.deduplicator.entities;

import java.util.List;

/**
 * @author Miroslav Kramolinski
 */
public class Thread {
    private String id;
    private RelatedQuestion relatedQuestion;
    private List<RelatedAnswer> relatedAnswers;
    private List<RelatedComment> relatedComments;

    public Thread(String id, RelatedQuestion relatedQuestion, List<RelatedAnswer> relatedAnswers, List<RelatedComment> relatedComments) {
        this.id = id;
        this.relatedQuestion = relatedQuestion;
        this.relatedAnswers = relatedAnswers;
        this.relatedComments = relatedComments;
    }

    public String getId() {
        return id;
    }

    public String getOriginalQuestionId() {
        return id.split("_")[0];
    }

    public String getRelatedQuestionId() {
        return id.split("_")[1].substring(1);
    }

    public RelatedQuestion getRelatedQuestion() {
        return relatedQuestion;
    }

    public List<RelatedAnswer> getRelatedAnswers() {
        return relatedAnswers;
    }

    public List<RelatedComment> getRelatedComments() {
        return relatedComments;
    }

    public RelatedComment getRelatedCommentById(String id) {
        for(RelatedComment relComment : relatedComments) {
            if(relComment.getId().equals(id))
                return relComment;
        }

        return null;
    }
}
