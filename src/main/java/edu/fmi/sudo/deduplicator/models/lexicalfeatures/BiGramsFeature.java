/**
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

public class BiGramsFeature extends MatchingWordsFeature {
    private IntersectionFinder intersectionFinder;


    public BiGramsFeature() {
        super();
        this.intersectionFinder = new IntersectionFinder(2, true);
    }
    @Override
    public void process() {
        findIntersectionSizes(this.intersectionFinder);
    }
}
