/**
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.dal;

import com.google.gson.Gson;
import com.mongodb.*;
import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalDataAccessFactory extends DataAccessFactory {
    private final String url = "localhost";
    private int port = 27017;
    private DBIterator<QuestionAnswers> trainCursor = new DBIterator<>(QuestionAnswers.class);
    private DBIterator<QuestionAnswers> testCursor = new DBIterator<>(QuestionAnswers.class);

    @Override
    public void prepareDB() {
        client = new MongoClient(url, port);
        this.database = client.getDatabase(DataAccessFactory.DATABASE_NAME);
    }

    public void close() {
        client.close();
    }

    public <T> void insertObject(String collName, T obj) {
        insertObjects(collName, Arrays.asList(obj));
    }

    public <T> void insertObjects(String collName, List<T> objList) {
        if(database == null)
            throw new MongoClientException("Database connection not active");

        Gson gson = new Gson();
        List<Document> documents = new ArrayList<>();
        for(T obj: objList) {
            documents.add(Document.parse(gson.toJson(obj)));
        }

        database.getCollection(collName).insertMany(documents);
    }

    public <T> DBIterator<T> getCursorToCollection(String collName, Class<T> clazz) {
        DBIterator<T> it = new DBIterator<T>(clazz);
        it.open(database, collName);
        return it;
    }

    public void initializeTrainCursor() {
        trainCursor.open(database, Collection.QUESTION_ANSWERS.getName(), new BasicDBObject("train", true));
    }

    public void initializeTestCursor() {
        testCursor.open(database, Collection.QUESTION_ANSWERS.getName(), new BasicDBObject("train", false));
    }

    public boolean hasNextTrain() {
        return trainCursor != null && trainCursor.hasNext();
    }

    public boolean hasNextTest() {
        return testCursor != null && testCursor.hasNext();
    }

    public QuestionAnswers getNextTrainEntry() {
        return trainCursor.next();
    }

    public QuestionAnswers getNextTestEntry() {
        return testCursor.next();
    }
}
