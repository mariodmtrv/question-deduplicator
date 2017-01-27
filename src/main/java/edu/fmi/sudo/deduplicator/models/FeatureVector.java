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
    TrainDataLabel labelFeature;
    boolean isTrain;
    private QuestionAnswers qa;
    Integer qaNo;

    public FeatureVector(QuestionAnswers qa, boolean isTrain, Integer qaNo) {
        this.isTrain = isTrain;
        this.qa = qa;
        this.qaNo = qaNo;
        this.vectorMetadata = new VectorMetadataFeature();
        if (isTrain) {
            this.labelFeature = new TrainDataLabel();
        }
    }

    private List<String> toMatrix() {
        List<String> matrix = new ArrayList<>();
        int entriesCount = features.get(0).toVector().size();
        for (int index = 0; index < entriesCount; index++) {
            StringBuilder entryResult = new StringBuilder();
            final int id = index;
            features.stream().forEach(feature -> entryResult.append(feature.getEntryValue(id) + ", "));
            String completeVector = entryResult.toString();
            completeVector = completeVector.substring(0, completeVector.length() - 1);
            List<String> featureValues = Arrays.asList(completeVector.split(","));
            Optional<String> reducedEntry = IntStream.range(1, featureValues.size()).mapToObj(entryId -> entryId + ":" + featureValues.get(entryId).trim()).reduce((a, b) -> a + " " + b);
            String mappedEntry = reducedEntry.isPresent()? reducedEntry.get(): "";

            if (isTrain) {
                mappedEntry = String.format("%s qid:%s %s", labelFeature.getEntryValue(index), qaNo.toString(), mappedEntry);
            }
            else{
                //indicate no preference in ranking
                mappedEntry = String.format("%s qid:%s %s", 0,  qaNo.toString(), mappedEntry);

            }

            mappedEntry = mappedEntry + " " + vectorMetadata.getEntryValue(index);
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

    public void setFeatures(List<Feature> features) {
        this.features = features;
        this.features.forEach(f -> {
            f.setQuestionAnswers(qa);
        });
        vectorMetadata.setQuestionAnswers(qa);
        if (isTrain) {
            labelFeature.setQuestionAnswers(qa);
        }
    }
}
