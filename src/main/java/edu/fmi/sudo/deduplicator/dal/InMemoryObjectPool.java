/**
 * Used to access objects kept in memory for performance
 *
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.dal;

import java.util.ArrayList;
import java.util.List;

import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;

public class InMemoryObjectPool {
    List<QuestionAnswers> availableObjects = new ArrayList<>();

    public void loadData(DataAccessFactory factory) {
        this.availableObjects.add(null);
        throw new UnsupportedOperationException("Not implemented");
    }

    public QuestionAnswers getEntity() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
