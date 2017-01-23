/**
 * @author Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.models.Feature;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CosSimilarityFeature extends Feature {
    @Override
    public void process() {
        Map<String, Integer> originalQuestionMap =
                generateFrequencyMap(this.questionAnswers.getQuestion().getTokens());
        Stream<Pair> relatedQuestionsSimilarities =
                this.questionAnswers
                        .getThreads().stream()
                        .map(thread -> {
                            Double questionSimilarity =
                                    computeCosSimilarity(originalQuestionMap,
                                            thread.getRelatedQuestion().getTokens());
                            List<String> joinedAnswers = thread.getRelatedAnswers().stream()
                                    .map(question -> question.getTextTokens())
                                    .flatMap(x -> x.stream())
                                    .collect(Collectors.toList());
                            Double answersSimilarity =
                                    computeCosSimilarity(originalQuestionMap, joinedAnswers);
                            return new Pair(questionSimilarity, answersSimilarity);
                        });
        List<String> normalizedQuestionSimilarities =
                Feature.normalizeValues(relatedQuestionsSimilarities
                        .map(pair -> (Double) pair.getKey())
                        .collect(Collectors.toList()));
        List<String> normalizedAnswerSimilarities =
                Feature.normalizeValues(relatedQuestionsSimilarities
                        .map(pair -> (Double) pair.getValue())
                        .collect(Collectors.toList()));
        featureValue = IntStream.range(0, normalizedQuestionSimilarities.size())
                .mapToObj(id ->
                        normalizedQuestionSimilarities.get(id) + ", "
                                + normalizedAnswerSimilarities.get(id))
                .collect(Collectors.toList());

    }


    private Double computeCosSimilarity(
            Map<String, Integer> originalQuestionMap,
            List<String> other) {
        return computeCosSimilarity(originalQuestionMap, generateFrequencyMap(other));
    }

    private Double computeCosSimilarity(Map<String, Integer> orig, Map<String, Integer> other) {
        Double result = Double.valueOf(
                orig.entrySet().stream()
                        .map(entry -> {
                            if (other.containsKey(entry.getKey())) {
                                return entry.getValue() * other.get(entry.getKey());
                            }
                            return 0;
                        }).reduce(Integer::sum).get());
        result /= (getNorm(orig) * getNorm(other));
        return result;
    }

    private Double getNorm(Map<String, Integer> vector) {
        Integer squaredSum = vector.entrySet().stream()
                .map(entry -> entry.getValue() * entry.getValue())
                .reduce(Integer::sum).get();
        return Math.sqrt(squaredSum);
    }

    private Map<String, Integer> generateFrequencyMap(List<String> tokens) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        tokens.stream().forEach(token -> {
            Integer count = frequencyMap.get(token);
            if (count == 0) {
                frequencyMap.put(token, 1);
            } else {
                frequencyMap.put(token, count + 1);
            }
        });
        return frequencyMap;
    }
}
