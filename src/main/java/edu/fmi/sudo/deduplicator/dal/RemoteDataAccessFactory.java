/**
 * @author Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.dal;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class RemoteDataAccessFactory extends DataAccessFactory {
    private final String url = "";
    private int port;

    @Override
    public void prepareDB() {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient();
            DB db = mongoClient.getDB(DataAccessFactory.DATABASE_NAME);
            boolean auth = db.authenticate("username", "password".toCharArray());
            this.database = db;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
