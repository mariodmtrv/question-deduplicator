package edu.fmi.sudo.deduplicator.entities;

import java.util.Date;
import java.util.List;

/**
 * @author Miroslav Kramolinski
 */

public class RelatedQuestion implements Question, Related {
    /*
     * Attributes of a related question
     */
    private String id;
    private String category;
    private Date date;

    private Integer rankingOrder;
    private Integer score;
    private Integer viewCount;
    private Relevance relevanceToOriginalQuestion;
    private List<String> tags;

    private Integer userId;
    private String username;

    private List<String> tokens;

    /*
     * Structure of a related question
     */
    private String subject;
    private String body;

    public RelatedQuestion(
            String id,
            String category,
            Date date,
            Integer rankingOrder,
            Integer score,
            Integer viewCount,
            Relevance relevance,
            List<String> tags,
            Integer userId,
            String username,
            String subject,
            String body) {
        this.id = id;
        this.category = category;
        this.date = date;
        this.rankingOrder = rankingOrder;
        this.score = score;
        this.viewCount = viewCount;
        this.relevanceToOriginalQuestion = relevance;
        this.tags = tags;
        this.userId = userId;
        this.username = username;
        this.subject = subject;
        this.body = body;
    }

    public RelatedQuestion() {

    }

    public RelatedQuestion(String id, Relevance relevance) {
        this.id = id;
        this.relevanceToOriginalQuestion = relevance;
    }

    @Override
    public String getId() {
        return id.split("_")[1].substring(1);
    }

    @Override
    public String getOriginalQuestionId() {
        return id.split("_")[0];
    }

    @Override
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public Relevance getRelevanceToOriginalQuestion() {
        return relevanceToOriginalQuestion;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getCategory() {
        return category;
    }

    public Integer getRankingOrder() {
        return rankingOrder;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) { this.tags = tags; }

    public Integer getViewCount() {
        return viewCount;
    }

    @Override
    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

}
