/**
 * Copyright 2017 (C) Endrotech
 * Created on :  1/14/2017
 * Author     :  Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.models.semantic_features;

import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;
import edu.fmi.sudo.deduplicator.models.Feature;

public class SampleSemanticFeature extends Feature {
    public SampleSemanticFeature() {
        super();
    }

    @Override
    public void process() {
        this.featureValue = "1";
    }
}
