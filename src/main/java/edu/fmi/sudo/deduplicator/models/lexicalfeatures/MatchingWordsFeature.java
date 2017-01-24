/**
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

public class MatchingWordsFeature extends WordCountingFeature {
    public MatchingWordsFeature() {
        super();
        this.intersectionFinder = new IntersectionFinder(1, true);
    }

    @Override
    public void process() {
        findIntersectionSizes(this.intersectionFinder);
    }


}
