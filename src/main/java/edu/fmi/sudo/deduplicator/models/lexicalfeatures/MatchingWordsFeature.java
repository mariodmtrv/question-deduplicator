/**
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.entities.*;
import edu.fmi.sudo.deduplicator.entities.Thread;
import edu.fmi.sudo.deduplicator.models.Feature;

import java.util.ArrayList;
import java.util.List;

public class MatchingWordsFeature extends Feature {
    private IntersectionFinder intersectionFinder;


    public MatchingWordsFeature() {
        super();
        this.intersectionFinder = new IntersectionFinder(1, true);
    }

    @Override
    public void process() {
        findIntersectionSizes(this.intersectionFinder);
    }

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
            Double answerValue = 0.0;
            featureValue.add(questionValue.toString());
            if (answers != null) {
                for (RelatedAnswer answer : answers) {
                    Double answerIntersection = intersectionFinder.getIntersectionSize(answer.getTextTokens());
                    Double answerCommentsIntersection = getCommentsIntersectionSize(answer.getRelatedComments());
                    answerValue += (answerIntersection + answerCommentsIntersection);
                }
                featureValue.add(", " + answerValue);
            }
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
