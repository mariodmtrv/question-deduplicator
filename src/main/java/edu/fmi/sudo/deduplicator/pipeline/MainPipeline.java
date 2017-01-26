package edu.fmi.sudo.deduplicator.pipeline;

import edu.fmi.sudo.deduplicator.Logger;
import edu.fmi.sudo.deduplicator.dal.LocalDataAccessFactory;
import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;
import edu.fmi.sudo.deduplicator.models.Feature;
import edu.fmi.sudo.deduplicator.models.FeatureVector;
import edu.fmi.sudo.deduplicator.models.lexicalfeatures.*;
import edu.fmi.sudo.deduplicator.training.DataSetGenerator;
import edu.fmi.sudo.deduplicator.training.DataSetType;
import edu.fmi.sudo.deduplicator.training.SvmClassifierAdapter;
import edu.fmi.sudo.deduplicator.training.SvmLearnerAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The pipeline's backbone, used to wire the enabled pipeline features
 */
public class MainPipeline {
    // defaults
    private List<PipelinePreProcessTask> pipelinePreProcessFeatures =
            Collections.unmodifiableList(
                    Arrays.asList(
                            PipelinePreProcessTask.GENERAL_STOPWORDS_REMOVAL,
                            PipelinePreProcessTask.SPECIALIZED_STOPWORDS_REMOVAL,
                            PipelinePreProcessTask.MAGIC_WORDS,
                            PipelinePreProcessTask.WSD,
                            PipelinePreProcessTask.POS_TAGGING,
                            PipelinePreProcessTask.TOKENIZATION
                    )
            );
    private LocalDataAccessFactory daf;
    private Long executionIdentifier = 1000L;
    // defaults
    private List<Feature> features =
            Collections.unmodifiableList(
                    Arrays.asList(  new BiGramsFeature()
                            , new CommonTagsFeature()
                            , new CosSimilarityFeature()
                            , new MatchingWordsFeature()
                            , new PosTaggingNounOverlapFeature()
                            , new PosTaggingProportionsFeature()
                            , new UserVotesFeature()));

    public MainPipeline(List<PipelinePreProcessTask> preProcessTasks, List<Feature> features, LocalDataAccessFactory daf) {
        this.pipelinePreProcessFeatures = preProcessTasks;
        this.features = features;
        this.daf = daf;
    }

    public void run(boolean train) {
        if(train) {
            Logger.log("INFO: TRAIN execution of Pipeline initiated");
            trainModel();
            Logger.log("INFO: TRAIN execution of Pipeline completed successfully");
        } else {
            Logger.log("INFO: TEST execution of Pipeline initiated");
            testGeneratedModel();
            Logger.log("INFO: TEST execution of Pipeline completed successfully");
        }
    }

    /**
     * Applies the pre-process features of the pipeline in the order given
     */
    private QuestionAnswers preProcess(QuestionAnswers qa) {

        try {
            for (PipelinePreProcessTask feature : pipelinePreProcessFeatures) {
                qa = PipelinePreProcessor.process(feature, qa);
            }

            return qa;
        } catch(RuntimeException e) { // expected exception time from pipeline filters
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // multithreaded execution...

    public void trainModel() {
        DataSetGenerator trainSetGenerator = new DataSetGenerator(DataSetType.TRAIN, executionIdentifier);
        QuestionAnswers questionAnswer = null;

        daf.initializeTrainCursor();
        while (daf.hasNextTrain()) {
            questionAnswer = preProcess(daf.getNextTrainEntry());
            FeatureVector featureVector = new FeatureVector(questionAnswer, true);
            featureVector.setFeatures(features);
            trainSetGenerator.writeEntry(featureVector.getValues());
        }
        daf.closeTrainCursor();

        SvmLearnerAdapter learner = new SvmLearnerAdapter(executionIdentifier);
        learner.execute();
    }

    public void testGeneratedModel() {
        DataSetGenerator testSetGenerator = new DataSetGenerator(DataSetType.TEST, executionIdentifier);
        QuestionAnswers questionAnswers = null;

        daf.initializeTestCursor();
        while (daf.hasNextTest()) {
            questionAnswers = preProcess(daf.getNextTestEntry());
            FeatureVector featureVector = new FeatureVector(questionAnswers, false);
            featureVector.setFeatures(features);
            testSetGenerator.writeEntry(featureVector.getValues());
        }
        daf.closeTestCursor();

        SvmClassifierAdapter classifier = new SvmClassifierAdapter(executionIdentifier);
        classifier.execute();
    }
}
