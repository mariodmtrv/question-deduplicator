/**
 * @author Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.dal;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class RemoteDataAccessFactory extends DataAccessFactory {
    private final String url = "";
    private int port;

    @Override
    public void prepareDB() {
        MongoClient mongoClient = null;
        mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase(DataAccessFactory.DATABASE_NAME);
    }
}
