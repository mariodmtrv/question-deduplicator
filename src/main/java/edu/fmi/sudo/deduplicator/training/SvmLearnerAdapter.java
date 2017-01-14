/**
 * Copyright 2017 (C) Endrotech
 * Created on :  1/14/2017
 * Author     :  Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.training;

public class SvmLearnerAdapter extends SvmAdapter {
    public SvmLearnerAdapter() {
        this.executablePath = "src\\main\\resources\\modules\\svm\\svm_rank_learn.exe";
    }
}
