package edu.fmi.sudo.deduplicator.training;

public enum DataSetType {
    TEST("test\\vector-%s.test"), TRAIN("train\\vector-%s.train");
    String pattern;

    DataSetType(String pattern) {
        this.pattern = pattern;
    }
}