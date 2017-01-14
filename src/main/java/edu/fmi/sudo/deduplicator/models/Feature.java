/**
 * General extracted feature
 *
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.models;

import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;

import java.io.Serializable;

public abstract class Feature implements Serializable {
    protected QuestionAnswers questionAnswers;
    protected String featureValue;

    public Feature() {
    }

    public void setQuestionAnswers(QuestionAnswers qa) {
        this.questionAnswers = qa;
    }

    public abstract void process();

    public String toString() {
        if (featureValue == null) {
            this.process();
        }
        return featureValue;
    }
}
