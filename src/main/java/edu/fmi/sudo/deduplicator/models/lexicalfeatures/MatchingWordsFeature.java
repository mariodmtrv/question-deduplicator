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
        intersectionFinder.setSourceEntityWords(originalQuestion.getSubject() + " " + originalQuestion.getBody());
        List<Thread> threads = this.questionAnswers.getThreads();
        for (Thread thread : threads) {
            Double questionIntersection =
                    intersectionFinder
                            .getIntersectionSize(thread.getRelatedQuestion().getSubject() + " " +
                                    thread.getRelatedQuestion().getBody());
            Double questionCommentsIntersection = getCommentsIntersectionSize(thread.getRelatedComments());
            Double questionValue = questionIntersection + questionCommentsIntersection;
            List<RelatedAnswer> answers = thread.getRelatedAnswers();
            if (answers != null) {
                for (RelatedAnswer answer : answers) {
                    Double answerIntersection = intersectionFinder.getIntersectionSize(answer.getText());
                    Double answerCommentsIntersection = getCommentsIntersectionSize(answer.getRelatedComments());
                    Double answerValue = answerIntersection + answerCommentsIntersection;
                    featureValue.add(questionValue + ", " + answerValue);
                }
            } else {
                featureValue.add(questionValue.toString());
            }
        }
    }

    private Double getCommentsIntersectionSize(List<RelatedComment> comments) {
        if(comments == null){
            return 0.0;
        }
        return comments.stream()
                .map(comment -> intersectionFinder.getIntersectionSize(comment.getText()))
                .reduce((a, b) -> a + b).get();
    }
}
