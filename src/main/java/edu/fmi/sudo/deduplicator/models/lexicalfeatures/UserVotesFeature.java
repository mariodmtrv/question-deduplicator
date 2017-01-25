/**
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.models.Feature;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserVotesFeature extends Feature {
    @Override
    public void process() {
        List<Double> values =
                this.questionAnswers
                        .getThreads()
                        .stream()
                        .map(thread -> {
                            Optional<Integer> reduced = thread
                                    .getRelatedAnswers()
                                    .stream()
                                    .map(answer -> answer.getScore())
                                    .reduce((a, b) -> a + b);

                            if(reduced.isPresent())
                                return Double.valueOf(reduced.get());
                            else
                                return 0.0;
                        })
                        .collect(Collectors.toList());
        this.featureValue = Feature.normalizeValues(values);
    }
}
