package edu.fmi.sudo.deduplicator;

import edu.fmi.sudo.deduplicator.config.Configuration;
import edu.fmi.sudo.deduplicator.dal.XMLReader;
import edu.fmi.sudo.deduplicator.evaluation.Evaluator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        if(args.length != 2) {
            printError("Incorrect number of arguments");
            return;
        }

        if(!args[0].equals("train") && !args[0].equals("test")) {
            printError("Must define if the execution is a train or test one");
            return;
        }

        // Read properties from deduplicator.properties file located alongside the jar
        readProperties();

        String filename = args[1];
        System.out.println("INFO: Input file set to: " + filename);

        if(args[0].equals("train"))
            train(Paths.get(filename).toString());
        else
            test(Paths.get(filename).toString());
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

    private static void train(String filename) {
        XMLReader.readFile(filename, true);
    }

    private static void test(String filename) {
        XMLReader.readFile(filename, false);

        String pathToSVM = ".\\predictions";
        Evaluator evaluator = new Evaluator();
        //data should be the original question-related question pairs, ordered as in the file with the predictions.
        //evaluator.evaluate(data, pathToSVM, "android");
    }

    private static void printError(String message) {
        System.out.println("ERROR: " + message + "; Usage:");
        System.out.println();
        System.out.println("    java -jar deduplicator.jar <train|test> <path_to_data_file>");
        System.out.println();
    }
}
