package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import gate.AnnotationSet;
import gate.Document;
import gate.util.SimpleFeatureMapImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by mateev on 19.1.2017 г..
 */
public class PosTaggingNounOverlapFeature extends PosTaggingFeature {
    @Override
    public String processFeatureMetrics(
            Document orgQuestionSubjectDoc,
            Document orgQuestionBodyDoc,
            Document relQuestionSubjectDoc,
            Document relQuestionBodyDoc) {

        List<Double> subjectValues = this.calculateNounOverlapValues(
                orgQuestionSubjectDoc,
                relQuestionSubjectDoc);

        List<Double> bodyValues = this.calculateNounOverlapValues(
                orgQuestionBodyDoc,
                relQuestionBodyDoc);

        List<Double> values = new ArrayList<>(subjectValues);
        values.addAll(bodyValues);

        //TODO: Should we do this now?
        String vector = getNormalizedVector(values);

        return  vector;
    }

    private String getNormalizedVector(List<Double> values) {
        Double mean = values.stream().mapToDouble(x -> x).average().getAsDouble();
        double diff = values.stream().max(Double::compareTo).get()
                - values.stream().min(Double::compareTo).get();

        return values.stream()
                .map(v -> diff == 0 ? 0 : (v - mean) / diff)
                .map(v -> Double.toString(v))
                .collect(Collectors.joining(" "));
    }

    private List<Double> calculateNounOverlapValues(Document doc1, Document doc2) {
        SimpleFeatureMapImpl hm = new SimpleFeatureMapImpl();
        hm.put("category", "NN");

        AnnotationSet doc1NounsSet = doc1.getAnnotations()
                .get("Token", hm);

        AnnotationSet doc2NounsSet = doc2.getAnnotations()
                .get("Token", hm);

        Set<String> doc1Nouns = doc1NounsSet
                .stream()
                .map(
                        (annotation) -> annotation.getFeatures().get("string").toString())
                .collect(Collectors.toSet());

        Set<String> doc2Nouns = doc2NounsSet
                .stream()
                .map(
                        (annotation) -> annotation.getFeatures().get("string").toString())
                .collect(Collectors.toSet());

        doc1Nouns.retainAll(doc2Nouns);
        int overlappingCount = doc1Nouns.size();

        double doc1NormByLength = ((double) overlappingCount) / doc1.getAnnotations().get("Token").size();
        double doc2NormByLength = ((double)overlappingCount) / doc2.getAnnotations().get("Token").size();

//        float mean = (doc1NormByLength + doc2NormByLength) / 2;
//        float diff = doc1NormByLength - doc2NormByLength;
//
//        float doc1Norm = diff == 0 ? 0 : (doc1NormByLength - mean) / diff;
//        float doc2Norm = diff == 0 ? 0 : (doc2NormByLength - mean) / diff;

        //return Arrays.asList(doc1Norm, doc2Norm);
        return Arrays.asList(doc1NormByLength, doc2NormByLength);
    }
}
