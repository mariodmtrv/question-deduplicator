/**
 * Stores statistical quality data for created models
 *
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.evaluation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelQuality {
    Map<String, Object> metrics;
    List<String> featuresUsed;
    Date dateExecuted;

    public ModelQuality() {
        this.metrics = new HashMap<>();
        this.featuresUsed = new ArrayList<>();
        this.dateExecuted = new Date();
    }

    ModelQuality metric(String name, Object value) {
        this.metrics.put(name, value);
        return this;
    }

    ModelQuality feature(String name) {
        this.featuresUsed.add(name);
        return this;
    }

    public String build() {
        StringBuilder result = new StringBuilder();
        metrics.entrySet().stream().map(entity ->
                result.append(String.format("name: %s  value: %s \n",
                        entity.getKey(), entity.getValue())));
        featuresUsed.stream().map(feature ->
                result.append(String.format("name: %s \n", feature)));
        result.append("created:" + dateExecuted.toString());
        return result.toString();
    }

}
