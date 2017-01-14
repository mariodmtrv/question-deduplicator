/**
 * Copyright 2017 (C) Endrotech
 * Created on :  1/14/2017
 * Author     :  Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator;

import edu.fmi.sudo.deduplicator.models.lexicalfeatures.IntersectionFinder;
import edu.fmi.sudo.deduplicator.models.lexicalfeatures.MatchingWordsFeature;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FeaturesTest {
    @Test
    public void testMatchingWordsFeature() {
        MatchingWordsFeature wordsFeature = new MatchingWordsFeature();
        wordsFeature.process();
    }

    @Test
    public void testIntersectionFinder() {
        IntersectionFinder finder
                = new IntersectionFinder(
                Arrays.asList("This is a red car".split(" ")), 2);
        Long size = finder
                .getIntersectionSize(
                        Arrays.asList("That is a red car too".split(" ")));
        assert (size == 2);
    }
}
