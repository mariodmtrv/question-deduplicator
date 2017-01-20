package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.models.lexicalfeatures.IntersectionFinder;
import edu.fmi.sudo.deduplicator.models.lexicalfeatures.MatchingWordsFeature;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class WordMatchesTest {
    @Test
    public void testMatchingWordsFeature() {
        MatchingWordsFeature wordsFeature = new MatchingWordsFeature();
        wordsFeature.process();
    }

    @Test
    public void testBiGramsFeature() {
        BiGramsFeature wordsFeature = new BiGramsFeature();
        wordsFeature.process();
    }

    @Test
    public void testIntersectionFinder() {
        IntersectionFinder finder
                = new IntersectionFinder(2, false);
        finder.setSourceEntityWords("This is a red car");
        Double size = finder
                .getIntersectionSize("That is a red car too");
        assert (size - 2.0 < 0.001);
    }
}
