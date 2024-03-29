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
    private boolean train;

    public QuestionAnswers(OriginalQuestion question, Thread thread) {
        this(question, thread, true);
    }

    public QuestionAnswers(OriginalQuestion question, List<Thread> threads) {
        this(question, threads, true);
    }

    public QuestionAnswers(OriginalQuestion question, Thread thread, boolean train) {
        this.question = question;
        relatedThreads = new ArrayList<>();
        relatedThreads.add(thread);
        this.train = train;
    }

    public QuestionAnswers(OriginalQuestion question, List<Thread> threads, boolean train) {
        this.question = question;
        relatedThreads = threads;
        this.train = train;
    }

    public OriginalQuestion getQuestion() {
        return question;
    }

    public void setQuestion(OriginalQuestion question) {
        this.question = question;
    }

    public List<RelatedAnswer> getAllRelatedAnswers() {
        List<RelatedAnswer> answers = new ArrayList<>();
        relatedThreads.stream().filter(t -> t.getRelatedAnswers() != null).forEach(t -> answers.addAll(t.getRelatedAnswers()));
        return answers;
    }

    public List<RelatedAnswer> getRelatedAnswers(String threadId) {
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

    public List<RelatedComment> getAllRelatedComments() {
        List<RelatedComment> relatedComments = new ArrayList<>();
        relatedThreads.stream().filter(t -> t.getRelatedComments() != null).forEach(t -> relatedComments.addAll(t.getRelatedComments()));
        return relatedComments;
    }

    public List<RelatedComment> getRelatedComments(String threadId) {
        for(Thread thread: relatedThreads)
            if(thread.getId().equals(threadId))
                return thread.getRelatedComments();

        return null;
    }

    public List<RelatedQuestion> getAllRelatedQuestions() {
        List<RelatedQuestion> questions = new ArrayList<>();
        relatedThreads.stream().forEach(t -> questions.add(t.getRelatedQuestion()));

        return questions;
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

    public List<Thread> getThreads() {
        return this.relatedThreads;
    }

    public boolean isTrain() {
        return train;
    }

    public void setTrain(boolean train) {
        this.train = train;
    }
}
