/**
 * @author Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.dal;

public enum Collection {
    QUESTION_ANSWERS("question-answers");

    private String name;

    Collection(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
