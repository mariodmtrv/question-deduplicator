package edu.fmi.sudo.deduplicator.models.evaluationfeatures;

import edu.fmi.sudo.deduplicator.entities.RelatedQuestion;
import edu.fmi.sudo.deduplicator.models.Feature;

import java.util.ArrayList;

/**
 * Created by mateev on 24.1.2017 г..
 */
public class VectorMetadataFeature extends Feature {
    @Override
    public void process() {
        this.featureValue = new ArrayList<>();
        for (RelatedQuestion rq : this.questionAnswers.getAllRelatedQuestions()) {
            String value = String.format(
                    "# %s %s %s",
                    this.questionAnswers.getQuestion().getId(),
                    rq.getId(),
                    rq.getRelevanceToOriginalQuestion().toString());
            this.featureValue.add(value);
        }
    }
}
