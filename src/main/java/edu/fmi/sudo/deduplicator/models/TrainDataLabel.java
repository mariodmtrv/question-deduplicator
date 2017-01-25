/**
 * Copyright 2017 (C) Endrotech
 * Created on :  1/21/2017
 * Author     :  Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.models;

import edu.fmi.sudo.deduplicator.entities.RelatedQuestion;
import edu.fmi.sudo.deduplicator.entities.Relevance;

import java.util.stream.Collectors;

public class TrainDataLabel extends Feature {
    @Override
    public void process() {
        this.featureValue =
                this.questionAnswers
                        .getAllRelatedQuestions()
                        .stream()
                        .map(entry ->
                                getLabel(entry).toString())
                        .collect(Collectors.toList());
    }

    private String getLabel(RelatedQuestion entry) {
        Relevance relevance = entry.getRelevanceToOriginalQuestion();
        switch (relevance) {
            case PerfectMatch: {
                return "3";
            }
            case Related: {
                return "2";
            }
            case Irrelevant: {
                return "1";
            }
            default: {
                throw new IllegalStateException("Entry not labeled");
            }
        }
    }
}
