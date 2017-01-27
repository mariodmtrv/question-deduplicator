package edu.fmi.sudo.deduplicator.training;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SvmLearnerAdapter extends SvmAdapter {
    public SvmLearnerAdapter(Long identifier) {
        super(identifier);

        try {
            InputStream exeStream = getClass().getClassLoader()
                    .getResourceAsStream("modules/svm/svm_rank_learn.exe");

            Files.copy(exeStream, Paths.get("./svm_rank_learn.exe"), StandardCopyOption.REPLACE_EXISTING);

            this.executablePath = "./svm_rank_learn.exe";

            String trainDataFile = this.resourcesRootPath + String.format(DataSetType.TRAIN.pattern, identifier.toString());
            String modelFile = String.format(this.modelPath, identifier.toString());
            boolean createdModel = new File(modelFile.substring(0, modelFile.length() - modelFile.substring(modelFile.lastIndexOf('\\')).length())).mkdirs();

            this.params = new String[]{"-c 0.01", trainDataFile, modelFile};
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
