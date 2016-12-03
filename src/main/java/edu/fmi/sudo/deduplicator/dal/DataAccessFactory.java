package edu.fmi.sudo.deduplicator.dal;

import com.mongodb.DB;

public abstract class DataAccessFactory {
    protected static final String DATABASE_NAME = "deduplication";
    protected DB database;
    public abstract void prepareDB();

    public void addObject(String collectionName){
        database.getCollection(collectionName);

    }
}
