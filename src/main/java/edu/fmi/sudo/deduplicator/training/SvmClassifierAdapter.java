/**
 * Used to train new models
 */
package edu.fmi.sudo.deduplicator.training;

import edu.fmi.sudo.deduplicator.models.FeatureVector;

import java.io.File;

public class SvmClassifierAdapter extends SvmAdapter {
    private String predictionRel = "predictions\\result-%s.pred";

    public SvmClassifierAdapter(Long identifier) {
        super(identifier);
        this.executablePath = "src\\main\\resources\\modules\\svm\\svm_rank_classify.exe";
        String testDataFile = this.resourcesRootPath + String.format(DataSetType.TEST.pattern, identifier.toString());
        String modelFile = String.format(this.modelPath, identifier.toString());
        String predictionFile = this.resourcesRootPath + String.format(predictionRel, identifier.toString());
        boolean createdPredictions = new File(predictionFile.substring(0, predictionFile.length() - predictionFile.substring(predictionFile.lastIndexOf('\\')).length())).mkdirs();

        this.params = new String[]{testDataFile, modelFile, predictionFile};
    }
}
