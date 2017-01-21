/**
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.models.Feature;

import java.util.List;
import java.util.stream.Collectors;

public class UserVotesFeature extends Feature {
    @Override
    public void process() {
        List<Double> values =
                this.questionAnswers
                        .getThreads()
                        .stream()
                        .map(thread ->
                                (double) thread
                                        .getRelatedAnswers()
                                        .stream()
                                        .map(answer -> answer.getScore())
                                        .reduce((a, b) -> a + b).get())
                        .collect(Collectors.toList());
        this.featureValue = Feature.normalizeValues(values);
    }
}
