package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.entities.OriginalQuestion;
import edu.fmi.sudo.deduplicator.models.Feature;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kramolinski
 */
public class CommonTagsFeature extends Feature {
    @Override
    public void process() {
        final OriginalQuestion origQ = questionAnswers.getQuestion();

        List<Double> values =
                this.questionAnswers
                    .getAllRelatedQuestions().stream()
                    .map(relQ -> {
                        Optional<Double> res = relQ.getTags().stream()
                                    .map(tag -> (origQ.getTokens().contains(tag)? 1.0: 0.0))
                                    .reduce((a, b) -> a + b);
                            if(res.isPresent())
                                return res.get() / (double) relQ.getTags().size();
                            else
                                return 0.0;
                    })
                    .collect(Collectors.toList());

        this.featureValue = normalizeValues(values);
    }
}
