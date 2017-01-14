/**
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.dal;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class LocalDataAccessFactory extends DataAccessFactory {
    private final String url = "localhost";
    private int port = 27017;

    @Override
    public void prepareDB() {
        MongoClient mongo = new MongoClient(url, port);
        DB db = mongo.getDB(DataAccessFactory.DATABASE_NAME);
        this.database = db;
    }
}
