/**
 * General extracted feature
 *
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.models;

import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Feature{
    protected QuestionAnswers questionAnswers;
    protected List<String> featureValue;

    public Feature() {
    }

    public void setQuestionAnswers(QuestionAnswers qa) {
        this.questionAnswers = qa;
    }

    public abstract void process();

    /**
     * Produces A list of values per every triple
     * QorigQ1relA1rel,..QorigQ1relAnrel...,QorigQ2relA1rel...
     * */
    public List<String> toVector() {
        if (featureValue == null) {
            this.featureValue = new ArrayList<>();
            this.process();
        }
        return featureValue;
    }
}
