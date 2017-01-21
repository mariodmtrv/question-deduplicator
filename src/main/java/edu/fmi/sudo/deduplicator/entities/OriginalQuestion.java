package edu.fmi.sudo.deduplicator.entities;

import java.util.List;

/**
 * @author Miroslav Kramolinski
 */
public class OriginalQuestion implements Question {
    /*
     * Attributes of an original question
     */
    private String id;

    /*
     * Structure of an original question
     */
    private String subject;
    private String body;
    private List<String> tokens;
    public OriginalQuestion(String id, String subject, String body) {
        this.id = id;
        this.subject = subject;
        this.body = body;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }}
