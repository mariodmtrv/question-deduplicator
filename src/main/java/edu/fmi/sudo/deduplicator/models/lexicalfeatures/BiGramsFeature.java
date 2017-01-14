/**
 * Copyright 2017 (C) Endrotech
 * Created on :  1/14/2017
 * Author     :  Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.models.Feature;

import java.util.Arrays;

public class BiGramsFeature extends Feature {
    private IntersectionFinder intersectionFinder
            = new IntersectionFinder(Arrays.asList("question"), 2);

    @Override
    public void process() {
    }
}
