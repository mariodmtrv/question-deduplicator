/**
 * Copyright 2017 (C) Endrotech
 * Created on :  1/14/2017
 * Author     :  Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.models.semanticfeatures;

import edu.fmi.sudo.deduplicator.models.Feature;

import java.util.ArrayList;
import java.util.Arrays;

public class SampleSemanticFeature extends Feature {
    public SampleSemanticFeature() {
        super();
    }

    @Override
    public void process() {
        this.featureValue = Arrays.asList("1", "1", "2");
    }
}
