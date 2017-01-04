package edu.fmi.sudo.deduplicator.entities;

import java.util.Date;

/**
 * @author Miroslav Kramolinski
 */
public class RelatedComment implements Comment, Related {
    /*
     * Attributes of a related comment
     */
    private String id;
    private Date date;

    private Integer userId;
    private String username;

    private Integer score;

    /*
     * Structure of a related comment
     */
    private String text;

    public RelatedComment(String id, Date date, Integer userId, String username, Integer score, String text) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.username = username;
        this.score = score;
        this.text = text;
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
        return null;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String getText() {
        return text;
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
}
