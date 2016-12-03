package edu.fmi.sudo.deduplicator.dal;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;

public class LocalDataAccessFactory extends DataAccessFactory {
    private final String url = "localhost";
    private int port = 27017;

    @Override
    public void prepareClient() {
        try {
            MongoClient mongo = new MongoClient(url, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
