package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.GateWrapper;
import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;
import edu.fmi.sudo.deduplicator.models.Feature;
import gate.*;
import gate.util.GateException;
import gate.util.SimpleFeatureMapImpl;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by mateev on 18.1.2017 г..
 */
public class PosTaggingProportionsFeature extends PosTaggingFeature {

    @Override
    public void processFeatureMetrics(
            Document orgQuestionSubjectDoc,
            Document orgQuestionBodyDoc,
            Document relQuestionSubjectDoc,
            Document relQuestionBodyDoc) {
        SimpleFeatureMapImpl hm = new SimpleFeatureMapImpl();
        hm.put("category", "NN");

//        doc.getAnnotations()
//                .get("Token", hm);
    }
}
