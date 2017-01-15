/**
 * Copyright 2017 (C) Endrotech
 * Created on :  1/14/2017
 * Author     :  Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.training;

public class DataSetGenerator {
    private String rootPath = "src\\main\\resources\\";
    private DataSetType dataSetType;

    enum DataSetType {
        TRAIN("test\\vector-%s.test"), TEST("train\\vector-%s.train");
        String pattern;

        DataSetType(String pattern) {
            this.pattern = pattern;
        }
    }

    public DataSetGenerator(DataSetType dataSetType) {

    }
}
