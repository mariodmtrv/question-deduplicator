package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.pipeline.TokenizationFilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IntersectionFinder {
    Set<String> sourceEntityWordsSet;
    List<String> sourceEntityWords;
    int nGramLength;
    boolean shouldNormalize;

    public IntersectionFinder(int nGramLength, boolean shouldNormalize) {
        this.nGramLength = nGramLength;
        this.shouldNormalize = shouldNormalize;
    }

    public void setSourceEntityWords(List<String> sourceEntityWords) {
        this.sourceEntityWords = sourceEntityWords;
    }

    public void setSourceEntityWords(String entity) {

        this.sourceEntityWords = TokenizationFilter.tokenizeEntity(entity);
    }

    private Set<String> createSet() {
        Set<String> entityWordsSet = new HashSet<>(
                createNgrams(sourceEntityWords));
        return entityWordsSet;
    }

    private List<String> createNgrams(List<String> words) {
        if (nGramLength == 1) {
            return (List) (((ArrayList) words).clone());
        }
        List<String> nGrams = new ArrayList<>();
        for (int index = 0; index < words.size() - nGramLength; index++) {
            nGrams.add(words
                    .subList(index, index + nGramLength)
                    .stream()
                    .reduce((a, b) -> a + " " + b)
                    .get());
        }
        return nGrams;
    }

    public Double getIntersectionSize(List<String> targetWords) {
        if (sourceEntityWordsSet == null) {
            sourceEntityWordsSet = createSet();
        }
        List<String> targetNgrams = createNgrams(targetWords);
        Double intersectionSize = ((Long) (targetNgrams.stream()
                .filter(ngram -> sourceEntityWordsSet
                        .contains(ngram)).count())).doubleValue();
        if (shouldNormalize) {
            intersectionSize /= targetNgrams.size();
        }
        return intersectionSize;
    }

    public Double getIntersectionSize(String target) {
        List<String> tokenizedTarget = TokenizationFilter.tokenizeEntity(target);
        return getIntersectionSize(tokenizedTarget);
    }

}
