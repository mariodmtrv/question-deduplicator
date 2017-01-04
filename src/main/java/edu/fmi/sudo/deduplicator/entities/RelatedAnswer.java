package edu.fmi.sudo.deduplicator.entities;

import java.util.Date;
import java.util.List;

/**
 * @author Miroslav Kramolinski
 */
public class RelatedAnswer implements Comment, Related {
    /*
     * Attributes of a related answer
     */
    private String id;
    private Date date;

    private Integer userId;
    private String username;

    private Integer score;
    private boolean accepted; // whether the answer has been accepted by the person asking the question or not

    /*
     * Structure of a related answer
     */
    private String text;
    List<RelatedComment> relatedComments;

    public RelatedAnswer(
            String id,
            Date date,
            Integer userId,
            String username,
            Integer score,
            boolean accepted,
            String text,
            List<RelatedComment> relatedComments) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.username = username;
        this.score = score;
        this.accepted = accepted;
        this.text = text;
        this.relatedComments = relatedComments;
    }

    @Override
    public String getId() {
        return id.split("_")[2].substring(1);
    }

    @Override
    public String getOriginalQuestionId() {
        return id.split("_")[0];
    }

    @Override
    public String getRelatedQuestionId() {
        return id.split("_")[1].substring(1);
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // Always empty
    @Override
    public Relevance getRelevanceToRelated() {
        return null;
    }

    // Always empty
    public Relevance getRelevanceToOriginalQuestion() {
        return null;
    }

    @Override
    public int getScore() {
        return score;
    }

    public boolean isAccepted() {
        return accepted;
    }

    @Override
    public String getText() {
        return text;
    }

    public List<RelatedComment> getRelatedComments() {
        return relatedComments;
    }
}
