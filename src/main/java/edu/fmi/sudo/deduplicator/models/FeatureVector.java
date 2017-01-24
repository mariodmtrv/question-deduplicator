package edu.fmi.sudo.deduplicator.models;

import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;
import edu.fmi.sudo.deduplicator.models.evaluationfeatures.VectorMetadataFeature;
import edu.fmi.sudo.deduplicator.models.lexicalfeatures.BiGramsFeature;
import edu.fmi.sudo.deduplicator.models.lexicalfeatures.MatchingWordsFeature;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FeatureVector {
    List<Feature> features;
    VectorMetadataFeature vectorMetadata;
    TrainDataLabel label;
    boolean isTrain;
    private QuestionAnswers qa;
    public FeatureVector(QuestionAnswers qa, boolean isTrain) {
        this.isTrain = isTrain;
        this.qa = qa;
    }

    public void process() {
        features.forEach(f -> f.process());
        vectorMetadata.process();
        if(isTrain){
            label.process();
        }
    }

    private List<String> toMatrix() {
        List<String> matrix = new ArrayList<>();
        int entriesCount = features.get(0).featureValue.size();
        for (int index = 0; index < entriesCount; index++) {
            StringBuilder entryResult = new StringBuilder();
            final int id = index;
            features.stream().forEach(feature -> entryResult.append(feature.getEntryValue(id) + ", "));

            List<String> featureValues = Arrays.asList(entryResult.toString().split(","));
            String mappedEntry = IntStream.range(0, featureValues.size()).mapToObj(entryId -> entryId + ":" + featureValues.get(entryId)).reduce((a, b) -> a + " " + b).get();
            //add features with no label
            mappedEntry = vectorMetadata.getEntryValue(index) + mappedEntry;
            if (isTrain) {
                mappedEntry = mappedEntry + label.getEntryValue(index);
            }
            matrix.add(mappedEntry);
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
        features.forEach(f -> {
            f.setQuestionAnswers(qa);
        });
        vectorMetadata.setQuestionAnswers(qa);
        if(isTrain){
            label.setQuestionAnswers(qa);
        }
    }
}
