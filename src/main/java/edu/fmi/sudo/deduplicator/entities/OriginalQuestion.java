package edu.fmi.sudo.deduplicator.entities;

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
}
