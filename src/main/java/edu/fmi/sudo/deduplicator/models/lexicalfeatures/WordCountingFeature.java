/**
 * @author Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.entities.OriginalQuestion;
import edu.fmi.sudo.deduplicator.entities.RelatedAnswer;
import edu.fmi.sudo.deduplicator.entities.RelatedComment;
import edu.fmi.sudo.deduplicator.entities.Thread;
import edu.fmi.sudo.deduplicator.models.Feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class WordCountingFeature extends Feature {
    protected IntersectionFinder intersectionFinder;

    protected void findIntersectionSizes(IntersectionFinder intersectionFinder) {
        this.featureValue = new ArrayList<>();
        OriginalQuestion originalQuestion = this.questionAnswers.getQuestion();
        List<String> originalQuestionTokens = originalQuestion.getTokens();
        intersectionFinder.setSourceEntityWords(originalQuestionTokens);
        List<Thread> threads = this.questionAnswers.getThreads();
        List<Double> questionValues = new ArrayList<>();
        List<Double> answerValues = new ArrayList<>();
        for (Thread thread : threads) {
            List<String> relatedQuestionTokens = thread.getRelatedQuestion().getTokens();
            Double questionIntersection =
                    intersectionFinder
                            .getIntersectionSize(relatedQuestionTokens);
            Double questionCommentsIntersection = getCommentsIntersectionSize(thread.getRelatedComments());
            Double questionValue = questionIntersection;
            if (questionCommentsIntersection != null) {
                questionValue += questionCommentsIntersection;
                questionValue /= 2;
            }
            List<RelatedAnswer> answers = thread.getRelatedAnswers();
            questionValues.add(questionValue);
            if (answers != null) {
                Double answerValue = 0.0;
                for (RelatedAnswer answer : answers) {
                    Double answerIntersection = intersectionFinder.getIntersectionSize(answer.getTextTokens()) / (answer.getTextTokens().size());
                    Double answerCommentsIntersection = getCommentsIntersectionSize(answer.getRelatedComments());
                    answerValue += answerIntersection;
                    if (answerCommentsIntersection != null) {
                        answerValue += answerCommentsIntersection;
                        answerValue /= 2;
                    }
                }
                answerValues.add(answerValue);
            }
        }
        List<String> questionValuesNormalized = Feature.normalizeValues(questionValues);
        List<String> answerNormalizedValues = Feature.normalizeValues(answerValues);
        this.featureValue = IntStream.range(0, questionValues.size()).mapToObj(val -> questionValuesNormalized.get(val) + "," + answerNormalizedValues.get(val)).collect(Collectors.toList());
    }

    private Double getCommentsIntersectionSize(List<RelatedComment> comments) {
        if (comments == null || comments.size() == 0) {
            return null;
        }
        Optional<Double> reduced = comments.stream()
                .map(comment -> intersectionFinder.getIntersectionSize(comment.getTextTokens())/comment.getTextTokens().size())
                .reduce((a, b) -> a + b);

        if (reduced.isPresent())
            return reduced.get();
        else
            return 0.0;
    }
}
