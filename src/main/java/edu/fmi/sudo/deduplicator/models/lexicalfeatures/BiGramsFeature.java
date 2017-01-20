/**
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

public class BiGramsFeature extends MatchingWordsFeature {
    private IntersectionFinder intersectionFinder
            = new IntersectionFinder(2, false);

    public BiGramsFeature() {
        super();
    }
}
