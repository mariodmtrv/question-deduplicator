package edu.fmi.sudo.deduplicator.models;

import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;
import edu.fmi.sudo.deduplicator.models.lexicalfeatures.BiGramsFeature;
import edu.fmi.sudo.deduplicator.models.lexicalfeatures.MatchingWordsFeature;

import java.util.*;

public class FeatureVector {
    List<Feature> features;
    public FeatureVector(QuestionAnswers qa) {
        features.forEach(f -> {
            f.setQuestionAnswers(qa);
        });
    }

    public void process() {
        features.forEach(f -> f.process());
    }

    private List<String> toMatrix() {
        List<String> matrix = new ArrayList<>();
        int entriesCount = features.get(0).featureValue.size();
        for (int index = 0; index < entriesCount; index++) {
            StringBuilder entryResult = new StringBuilder();
            final int id = index;
            features.stream().forEach(feature -> entryResult.append(feature.getEntryValue(id) + ", "));
            matrix.add(entryResult.toString());
        }
        return matrix;
    }

    public String getFeaturesUsedList() {
        Optional<String> result = features.stream().map(f -> {
            String[] list = f.getClass().toString().split(".");
            return list[list.length - 1];
        }).reduce((a, b) -> a.toString() + ", " + b.toString());
        return result.get();
    }

    public List<String> getValues() {
        return toMatrix();
    }
    public void setFeatures( List<Feature> features){
        this.features = features;
    }
}
