/**
 * Copyright 2017 (C) Endrotech
 * Created on :  1/14/2017
 * Author     :  Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.models;

import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;
import edu.fmi.sudo.deduplicator.models.semantic_features.SampleSemanticFeature;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FeatureVector {
    List<Feature> features = Collections.unmodifiableList(Arrays.asList(new SampleSemanticFeature()));

    public FeatureVector(QuestionAnswers qa) {
        features.forEach(f -> {
            f.setQuestionAnswers(qa);
        });
    }

    public void process() {
        features.forEach(f -> f.process());
    }

    public String toString() {
        return features.stream()
                .map(f -> f.toString())
                .reduce((a, b) -> a + "," + b).get();
    }

    public String getFeaturesUsedList() {
        Optional<String> result = features.stream().map(f -> {
            String[] list = f.getClass().toString().split(".");
            return list[list.length - 1];
        }).reduce((a, b) -> a.toString() + ", " + b.toString());
        return result.get();
    }
}
