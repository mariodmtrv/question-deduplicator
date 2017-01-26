package edu.fmi.sudo.deduplicator.training;

import java.io.File;

public class SvmLearnerAdapter extends SvmAdapter {
    public SvmLearnerAdapter(Long identifier) {
        super(identifier);
        this.executablePath = "src\\main\\resources\\modules\\svm\\svm_rank_learn.exe";
        String trainDataFile = this.resourcesRootPath + String.format(DataSetType.TRAIN.pattern, identifier.toString());
        String modelFile = String.format(this.modelPath, identifier.toString());
        boolean createdModel = new File(modelFile.substring(0, modelFile.length() - modelFile.substring(modelFile.lastIndexOf('\\')).length())).mkdirs();

        this.params = new String[]{trainDataFile, modelFile};
    }
}
