/**
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.entities.Comment;
import edu.fmi.sudo.deduplicator.entities.RelatedAnswer;
import edu.fmi.sudo.deduplicator.entities.RelatedComment;
import edu.fmi.sudo.deduplicator.entities.Thread;
import edu.fmi.sudo.deduplicator.models.Feature;

import java.util.ArrayList;
import java.util.List;

public class MatchingWordsFeature extends Feature {
    private IntersectionFinder intersectionFinder
            = new IntersectionFinder(1, true);

    public MatchingWordsFeature() {
        super();
    }

    @Override
    public void process() {
        findIntersectionSizes();
    }

    protected void findIntersectionSizes() {
        this.featureValue = new ArrayList<>();
        intersectionFinder.setSourceEntityWords(this.questionAnswers.getQuestion().getBody());
        List<Thread> threads = this.questionAnswers.getThreads();
        for (Thread thread : threads) {
            Double questionIntersection =
                    intersectionFinder
                            .getIntersectionSize(
                                    thread.getRelatedQuestion().getBody());
            Double questionCommentsIntersection = getCommentsIntersectionSize(thread.getRelatedComments());
            Double questionValue = questionIntersection + questionCommentsIntersection;
            List<RelatedAnswer> answers = thread.getRelatedAnswers();
            for (RelatedAnswer answer : answers) {
                Double answerIntersection = intersectionFinder.getIntersectionSize(answer.getText());
                Double answerCommentsIntersection = getCommentsIntersectionSize(answer.getRelatedComments());
                Double answerValue = answerIntersection + answerCommentsIntersection;
                featureValue.add(questionValue + ", " + answerValue);
            }
        }
    }

    private Double getCommentsIntersectionSize(List<RelatedComment> comments) {
        return comments.stream()
                .map(comment -> intersectionFinder.getIntersectionSize(comment.getText()))
                .reduce((a, b) -> a + b).get();
    }
}
