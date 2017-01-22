package edu.fmi.sudo.deduplicator;

import edu.fmi.sudo.deduplicator.config.Configuration;
import edu.fmi.sudo.deduplicator.dal.XMLReader;
import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;
import edu.fmi.sudo.deduplicator.evaluation.Evaluator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        final String devData =
                args.length > 0?
                        args[0]:
                        "M:\\FMI\\SemEval\\DevData\\dev_set.txt"; // for testing

        // Read properties from deduplicator.properties file located alongside the jar
        readProperties();

        System.out.println("Input file set to: " + devData);
        List<QuestionAnswers> data = XMLReader.readFile(Paths.get(devData).toString());

        String pathToSVM = ".\\predictions";
        Evaluator evaluator = new Evaluator();
        //data should be the original question-related question pairs, ordered as in the file with the predictions.
        evaluator.evaluate(data, pathToSVM, "android");
    }

    private static void readProperties() {
        Properties properties = new Properties();
        try {
            FileInputStream file = new FileInputStream("./deduplicator.properties");
            properties.load(file);

            Configuration.setBasePath(properties.getProperty("base_path"));
        } catch (IOException e) {
            System.out.println("WARN: Unable to read properties file \"deduplicator.properties\". Setting jar location as base folder instead");
            File jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            Configuration.setBasePath(jarFile.getParentFile().getPath());
        }
    }
}
