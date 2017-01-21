package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import gate.AnnotationSet;
import gate.Document;
import gate.util.SimpleFeatureMapImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mateev on 18.1.2017 г..
 */
public class PosTaggingProportionsFeature extends PosTaggingFeature {

    @Override
    public String processFeatureMetrics(
            Document orgQuestionSubjectDoc,
            Document orgQuestionBodyDoc,
            Document relQuestionSubjectDoc,
            Document relQuestionBodyDoc) {

        List<Double> subjectValues = this.calculateProportions(
                orgQuestionSubjectDoc,
                relQuestionSubjectDoc);

        List<Double> bodyValues = this.calculateProportions(
                orgQuestionBodyDoc,
                relQuestionBodyDoc);

        List<Double> values = new ArrayList<>(subjectValues);
        values.addAll(bodyValues);

        //TODO: Should we do this now?
        String vector = getNormalizedVector(values);

        return  vector;
    }

    private List<Double> calculateProportions(Document doc1, Document doc2) {
        SimpleFeatureMapImpl hm = new SimpleFeatureMapImpl();
        hm.put("category", "NN");

        AnnotationSet doc1NounsSet = doc1.getAnnotations()
                .get("Token", hm);

        double doc1NounProportion = doc1NounsSet.size() / (double)doc1.getAnnotations().size();

        AnnotationSet doc2NounsSet = doc2.getAnnotations()
                .get("Token", hm);

        double doc2NounProportion = doc2NounsSet.size() / (double)doc2.getAnnotations().size();

        SimpleFeatureMapImpl jj = new SimpleFeatureMapImpl();
        hm.put("category", "JJ");

        AnnotationSet doc1AdjSet = doc1.getAnnotations()
                .get("Token", jj);

        double doc1AdjProportion = doc1AdjSet.size() / (double)doc1.getAnnotations().size();

        AnnotationSet doc2AdjSet = doc2.getAnnotations()
                .get("Token", jj);

        double doc2AdjProportion = doc2AdjSet.size() / (double)doc2.getAnnotations().size();

        SimpleFeatureMapImpl vb = new SimpleFeatureMapImpl();
        hm.put("category", "VB");

        AnnotationSet doc1VBSet = doc1.getAnnotations()
                .get("Token", vb);

        double doc1VBProportion = doc1VBSet.size() / (double)doc1.getAnnotations().size();

        AnnotationSet doc2VBSet = doc2.getAnnotations()
                .get("Token", vb);

        double doc2VBProportion = doc2VBSet.size() / (double)doc2.getAnnotations().size();

        return Arrays.asList(
                doc1NounProportion,
                doc2NounProportion,
                doc1AdjProportion,
                doc2AdjProportion,
                doc1VBProportion,
                doc2VBProportion);
    }
}
