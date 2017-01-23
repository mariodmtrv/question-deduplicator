package edu.fmi.sudo.deduplicator.dal;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by Miroslav on 1/22/2017.
 */
public class DBIterator<T> {
    MongoCursor cursor;
    Class<T> clazz;

    DBIterator(Class<T> clazz) {
        this.clazz = clazz;
    }

    public boolean hasNext() {
        return cursor != null && cursor.hasNext();
    }

    public T next() {
        if(hasNext()) {
            return (new Gson()).fromJson(((Document) cursor.next()).toJson(), clazz);
        }

        return null;
    }

    public void close() {
        if(cursor != null)
            cursor.close();
    }

    public void open(MongoDatabase db, String collName) {
        open(db, collName, null);
    }

    public void open(MongoDatabase db, String collName, BasicDBObject query) {
        cursor = query == null?
                db.getCollection(collName).find().iterator():
                db.getCollection(collName).find(query).iterator();
    }
}
