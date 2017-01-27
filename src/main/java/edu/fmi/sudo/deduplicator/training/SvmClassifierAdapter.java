/**
 * Used to train new models
 */
package edu.fmi.sudo.deduplicator.training;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SvmClassifierAdapter extends SvmAdapter {
    private String predictionRel = "predictions\\result-%s.pred";
    private String testDataFile;
    private String predictionFile;

    public SvmClassifierAdapter(Long identifier) {
        super(identifier);

        try {
            InputStream exeStream = getClass().getClassLoader()
                    .getResourceAsStream("modules/svm/svm_rank_classify.exe");

            Files.copy(exeStream, Paths.get("./svm_rank_classify.exe"), StandardCopyOption.REPLACE_EXISTING);

            this.executablePath = "./svm_rank_classify.exe";

            this.testDataFile = this.resourcesRootPath
                    + String.format(DataSetType.TEST.pattern, identifier.toString());

            String modelFile = String.format(this.modelPath, identifier.toString());

            this.predictionFile = this.resourcesRootPath + String.format(predictionRel, identifier.toString());

            boolean createdPredictions = new File(getPredictionFile().substring(0, getPredictionFile().length() - getPredictionFile().substring(getPredictionFile().lastIndexOf('\\')).length())).mkdirs();

            this.params = new String[]{this.getTestDataFile(), modelFile, getPredictionFile()};
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTestDataFile() {
        return testDataFile;
    }

    public String getPredictionFile() {
        return predictionFile;
    }
}
