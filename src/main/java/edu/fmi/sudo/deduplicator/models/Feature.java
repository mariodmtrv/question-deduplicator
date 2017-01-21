/**
 * General extracted feature
 *
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.models;

import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Feature {
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
     */
    public List<String> toVector() {
        if (featureValue == null) {
            this.featureValue = new ArrayList<>();
            this.process();
        }
        return featureValue;
    }

    public String getEntryValue(int id) {
        if (featureValue == null) {
            this.featureValue = new ArrayList<>();
            this.process();
        }
        if (id > featureValue.size()) {
            throw new IllegalStateException("Dimensionality mismatch");
        }
        return featureValue.get(id);
    }

    protected static List<String> normalizeValues(List<Double> values) {
        Double minValue = values.stream().min(Double::compareTo).get();
        Double maxValue = values.stream().max(Double::compareTo).get();
        List<String> normalizedValues = values.stream().map(value -> (value - minValue) / (maxValue - minValue)).map(Feature::truncateDecimal).collect(Collectors.toList());
        return normalizedValues;
    }

    private static String truncateDecimal(double x)
    {
        int numberOfDecimals = 3;
        if ( x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberOfDecimals, BigDecimal.ROUND_FLOOR).toString();
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberOfDecimals, BigDecimal.ROUND_CEILING).toString();
        }
    }
}
