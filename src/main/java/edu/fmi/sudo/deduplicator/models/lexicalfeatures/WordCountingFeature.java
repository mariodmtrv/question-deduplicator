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

public abstract class WordCountingFeature extends Feature {
    protected IntersectionFinder intersectionFinder;
    protected void findIntersectionSizes(IntersectionFinder intersectionFinder) {
        this.featureValue = new ArrayList<>();
        OriginalQuestion originalQuestion = this.questionAnswers.getQuestion();
        List<String> originalQuestionTokens = originalQuestion.getTokens();
        intersectionFinder.setSourceEntityWords(originalQuestionTokens);
        List<Thread> threads = this.questionAnswers.getThreads();
        for (Thread thread : threads) {
            List<String> relatedQuestionTokens = thread.getRelatedQuestion().getTokens();
            Double questionIntersection =
                    intersectionFinder
                            .getIntersectionSize(relatedQuestionTokens);
            Double questionCommentsIntersection = getCommentsIntersectionSize(thread.getRelatedComments());
            Double questionValue = questionIntersection + questionCommentsIntersection;
            List<RelatedAnswer> answers = thread.getRelatedAnswers();
            String threadFeatureValue = truncateDecimal(questionValue);
            if (answers != null) {
                Double answerValue = 0.0;
                for (RelatedAnswer answer : answers) {
                    Double answerIntersection = intersectionFinder.getIntersectionSize(answer.getTextTokens());
                    Double answerCommentsIntersection = getCommentsIntersectionSize(answer.getRelatedComments());
                    answerValue += (answerIntersection + answerCommentsIntersection);
                }
                threadFeatureValue += "," + truncateDecimal(answerValue);
            }
            featureValue.add(threadFeatureValue);
        }
    }

    private Double getCommentsIntersectionSize(List<RelatedComment> comments) {
        if (comments == null) {
            return 0.0;
        }
        return comments.stream()
                .map(comment -> intersectionFinder.getIntersectionSize(comment.getTextTokens()))
                .reduce((a, b) -> a + b).get();
    }
}
