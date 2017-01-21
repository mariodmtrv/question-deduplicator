package edu.fmi.sudo.deduplicator.training;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataSetGenerator {
    // well, not really...
    private String rootPath = "src\\main\\resources\\";
    private DataSetType dataSetType;
    private Long identifier;
    private BufferedWriter writer;


    public DataSetGenerator(DataSetType dataSetType, Long identifier) {
        this.dataSetType = dataSetType;
        String path = rootPath + String.format(dataSetType.pattern, identifier.toString());
        try {
            this.writer = new BufferedWriter(new FileWriter(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeEntry(List<String> entry) {
        entry.stream().forEach(entryLine -> {
            try {
                this.writer.write(entryLine);
                this.writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        try {
            this.writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

}
