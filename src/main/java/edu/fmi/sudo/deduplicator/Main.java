package edu.fmi.sudo.deduplicator;

import edu.fmi.sudo.deduplicator.config.Configuration;
import edu.fmi.sudo.deduplicator.dal.LocalDataAccessFactory;
import edu.fmi.sudo.deduplicator.dal.XMLReader;
import edu.fmi.sudo.deduplicator.models.lexicalfeatures.*;
import edu.fmi.sudo.deduplicator.pipeline.MainPipeline;
import edu.fmi.sudo.deduplicator.pipeline.PipelinePreProcessTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int supportedOptionsCount = 1; // [-v]
        String[] argv;

        if(args.length < 2 || args.length > 2 + supportedOptionsCount) {
            printError("Incorrect number of arguments");
            return;
        } else if(args.length == 2) {
            argv = args;
        } else {
            argv = new String[2];
            argv[1] = args[args.length - 1];
            argv[0] = args[args.length - 2];
            if(args[0].equals("-v") || args[0].equals("--verbose"))
                Logger.enableLogging();
            else {
                printError("Incorrect option provided");
                return;
            }
        }

        if(!argv[0].equals("train") && !argv[0].equals("test")) {
            printError("Must define if the execution is a train or test one");
            return;
        }

        // Read properties from deduplicator.properties file located alongside the jar
        readProperties();

        String filename = argv[1];
        Logger.log("INFO: Input file set to: " + filename);

        // XML file validation could be performed here (if not comment it out)
//        if(!XMLValidator.validate(filename))
//            throw new RuntimeException("FATAL: File validation failed");

        if(argv[0].equals("train"))
            train(Paths.get(filename).toString());
        else
            test(Paths.get(filename).toString());

        long end = System.currentTimeMillis();

        Logger.log("INFO: Execution time: " + (end - start) / 1000 + "s");
    }

    private static void readProperties() {
        Properties properties = new Properties();
        try {
            FileInputStream file = new FileInputStream("./deduplicator.properties");
            properties.load(file);

            Configuration.setBasePath(properties.getProperty("base_path"));
        } catch (IOException e) {
            Logger.log("WARN: Unable to read properties file \"deduplicator.properties\". Setting jar location as base folder instead");
            File jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            Configuration.setBasePath(jarFile.getParentFile().getPath());
        }
    }

    private static void train(String filename) {
        LocalDataAccessFactory daf = new LocalDataAccessFactory();
        daf.prepareDB();

        XMLReader.readFile(filename, true);
        MainPipeline pipeline = new MainPipeline(
                Collections.unmodifiableList(
                        Arrays.asList(
                                PipelinePreProcessTask.HTML_UNESCAPE,
                                PipelinePreProcessTask.GENERAL_STOPWORDS_REMOVAL,
                                PipelinePreProcessTask.SPECIALIZED_STOPWORDS_REMOVAL,
                                PipelinePreProcessTask.MAGIC_WORDS,
                                PipelinePreProcessTask.WSD,
                                PipelinePreProcessTask.POS_TAGGING,
                                PipelinePreProcessTask.TOKENIZATION
                        )
                ),
                Collections.unmodifiableList(
                        Arrays.asList(  new BiGramsFeature()
                                , new CommonTagsFeature()
                                , new CosSimilarityFeature()
                                , new MatchingWordsFeature()
                                , new PosTaggingNounOverlapFeature()
                                , new PosTaggingProportionsFeature()
                                , new UserVotesFeature()
                        )
                ),
                daf
        );

        pipeline.run(true);

        daf.close();
    }

    private static void test(String filename) {
        LocalDataAccessFactory daf = new LocalDataAccessFactory();
        daf.prepareDB();

        XMLReader.readFile(filename, false);
        MainPipeline pipeline = new MainPipeline(
                Collections.unmodifiableList(
                        Arrays.asList(
                                PipelinePreProcessTask.HTML_UNESCAPE,
                                PipelinePreProcessTask.GENERAL_STOPWORDS_REMOVAL,
                                PipelinePreProcessTask.SPECIALIZED_STOPWORDS_REMOVAL,
                                PipelinePreProcessTask.MAGIC_WORDS,
                                PipelinePreProcessTask.WSD,
                                PipelinePreProcessTask.POS_TAGGING,
                                PipelinePreProcessTask.TOKENIZATION
                        )
                ),
                Collections.unmodifiableList(
                        Arrays.asList(  new BiGramsFeature()
                                , new CommonTagsFeature()
                                , new CosSimilarityFeature()
                                , new MatchingWordsFeature()
                                , new PosTaggingNounOverlapFeature()
                                , new PosTaggingProportionsFeature()
                                , new UserVotesFeature()
                        )
                ),
                daf
        );

        pipeline.run(false);

        daf.close();
    }

    private static void printError(String message) {
        System.out.println("ERROR: " + message);
        System.out.println();
        System.out.println("usage:    java -jar deduplicator.jar [-v] <train|test> <path_to_data_file>");
        System.out.println();
        System.out.println("Options:");
        System.out.println(" -v, --verbose             Enables logging");
        System.out.println();
    }
}
