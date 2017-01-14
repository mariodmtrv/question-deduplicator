/**
 * Represents a question, related questions, related answers and comments
 *
 * @author Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.entities;

import java.util.ArrayList;
import java.util.List;

public class QuestionAnswers {
    private OriginalQuestion question;
    private List<Thread> relatedThreads;

    public QuestionAnswers(OriginalQuestion question, List<Thread> threads) {
        this.question = question;
        relatedThreads = threads;
    }

    public QuestionAnswers(OriginalQuestion question, Thread thread) {
        this.question = question;
        relatedThreads = new ArrayList<>();
        relatedThreads.add(thread);
    }

    public OriginalQuestion getQuestion() {
        return question;
    }

    public List<RelatedAnswer> getRelatedAnswer(String threadId) {
        for(Thread thread : relatedThreads) {
            if(thread.getId().equals(threadId))
                return thread.getRelatedAnswers();
        }

        return null;
    }

    public RelatedComment getRelatedCommentById(String id) {
        for(Thread thread : relatedThreads) {
            if(thread.getRelatedCommentById(id) != null)
                return thread.getRelatedCommentById(id);
        }

        return null;
    }

    public List<RelatedComment> getRelatedComments(String threadId) {
        for(Thread thread: relatedThreads)
            if(thread.getId().equals(threadId))
                return thread.getRelatedComments();

        return null;
    }

    public static <T extends Related> Object getRelated() {
//        System.out.println(  );


        return null;
    }

    public void addThread(Thread thread) {
        if(relatedThreads == null)
            relatedThreads = new ArrayList<>();
        else
            relatedThreads.add(thread);
    }
}
