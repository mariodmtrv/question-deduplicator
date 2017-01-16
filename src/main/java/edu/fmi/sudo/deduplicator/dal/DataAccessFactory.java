/**
 * Used to access persistent storage
 *
 * @author Mario Dimitrov
 *
 */
package edu.fmi.sudo.deduplicator.dal;

import com.mongodb.DB;
import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;

import java.util.List;

public abstract class DataAccessFactory {
    protected static final String DATABASE_NAME = "deduplication";
    protected DB database;

    public abstract void prepareDB();

    public void addObject(Collection collection){
        database.getCollection(collection.getName());

    }

    public List<QuestionAnswers> getAllObjects() {
        return null;
    }
}
