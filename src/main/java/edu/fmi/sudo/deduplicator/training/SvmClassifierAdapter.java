/**
 * Used to train new models
 *
 * @author Mario Dimitrov
 */
package edu.fmi.sudo.deduplicator.training;

import edu.fmi.sudo.deduplicator.models.FeatureVector;

public class SvmClassifierAdapter extends SvmAdapter {
    public SvmClassifierAdapter() {
        this.executablePath = "src\\main\\resources\\modules\\svm\\svm_rank_classify.exe";
    }
    public void addEntity(FeatureVector vector){
        vector.toString();
    }
}
