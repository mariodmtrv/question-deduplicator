package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IntersectionFinder {
    Set<String> sourceEntityWordsSet;
    List<String> sourceEntityWords;
    int nGramLength;

    public IntersectionFinder(List<String> sourceEntityWords, int nGramLength) {
        this.sourceEntityWords = sourceEntityWords;
        this.nGramLength = nGramLength;
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

    public Long getIntersectionSize(List<String> targetWords) {
        if (sourceEntityWordsSet == null) {
            sourceEntityWordsSet = createSet();
        }
        List<String> targetNgrams = createNgrams(targetWords);
        return targetNgrams.stream()
                .filter(ngram -> sourceEntityWordsSet
                        .contains(ngram)).count();
    }

}
