/**
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

public class BiGramsFeature extends WordCountingFeature {
    public BiGramsFeature() {
        super();
        this.intersectionFinder = new IntersectionFinder(2, true);
    }

    @Override
    public void process() {
        findIntersectionSizes(this.intersectionFinder);
    }
}
