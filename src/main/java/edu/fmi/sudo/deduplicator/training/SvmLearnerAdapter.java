package edu.fmi.sudo.deduplicator.training;

public class SvmLearnerAdapter extends SvmAdapter {
    public SvmLearnerAdapter(Long identifier) {
        super(identifier);
        this.executablePath = "src\\main\\resources\\modules\\svm\\svm_rank_learn.exe";
        String trainDataFile = this.resourcesRootPath + String.format(DataSetType.TRAIN.pattern, identifier.toString());
        String modelFile = String.format(this.modelPath, identifier.toString());
        this.params = new String[]{trainDataFile, modelFile};
    }
}
