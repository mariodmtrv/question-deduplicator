/**
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.dal;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class LocalDataAccessFactory extends DataAccessFactory {
    private final String url = "localhost";
    private int port = 27017;

    @Override
    public void prepareDB() {
        MongoClient mongo = new MongoClient(url, port);
        this.database = mongo.getDatabase(DataAccessFactory.DATABASE_NAME);
    }
}
