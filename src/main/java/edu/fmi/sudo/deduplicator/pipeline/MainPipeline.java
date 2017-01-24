package edu.fmi.sudo.deduplicator.pipeline;

import edu.fmi.sudo.deduplicator.dal.DataAccessFactory;
import edu.fmi.sudo.deduplicator.dal.LocalDataAccessFactory;
import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;
import edu.fmi.sudo.deduplicator.models.Feature;
import edu.fmi.sudo.deduplicator.models.FeatureVector;
import edu.fmi.sudo.deduplicator.models.TrainDataLabel;
import edu.fmi.sudo.deduplicator.models.lexicalfeatures.BiGramsFeature;
import edu.fmi.sudo.deduplicator.models.lexicalfeatures.MatchingWordsFeature;
import edu.fmi.sudo.deduplicator.training.DataSetGenerator;
import edu.fmi.sudo.deduplicator.training.DataSetType;
import edu.fmi.sudo.deduplicator.training.SvmClassifierAdapter;
import edu.fmi.sudo.deduplicator.training.SvmLearnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The pipeline's backbone, used to wire the enabled pipeline features
 */
public class MainPipeline {
    private List<PipelineFeature> enabledFeatures;
    private DataAccessFactory daf;
    private Long executionIdentifier = 1000L;
    // use pipeline features?
    private List<Feature> features =
            Collections.unmodifiableList(
                    Arrays.asList(new BiGramsFeature(), new MatchingWordsFeature()));

    MainPipeline() {
        enabledFeatures = new ArrayList<>();
    }

    MainPipeline(List<PipelineFeature> features, DataAccessFactory daf) {
        this.enabledFeatures = features;
        this.daf = daf;
    }

    /**
     * Applies the filters of the pipeline in the order the features are given
     */
    public List<QuestionAnswers> process() {
        List<QuestionAnswers> processed = new ArrayList<>();

        try {
            for(QuestionAnswers qa: daf.getAllObjects()) {
                for (PipelineFeature feature : enabledFeatures) {
                    qa = PipelineFilter.process(feature, qa);
                }

                processed.add(qa);
            }

            return processed;
        } catch(RuntimeException e) { // expected exception time from pipeline filters
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // multithreaded execution...

    public void trainModel(LocalDataAccessFactory dataAccessFactory) {
        DataSetGenerator trainSetGenerator = new DataSetGenerator(DataSetType.TRAIN, executionIdentifier);
        QuestionAnswers questionAnswer = null;
        while (dataAccessFactory.hasNextTrain()) {
            questionAnswer = dataAccessFactory.getNextTrainEntry();
            FeatureVector featureVector = new FeatureVector(questionAnswer, true);
            List<Feature> trainingFeatures = features;
            featureVector.setFeatures(trainingFeatures);
            featureVector.process();
            trainSetGenerator.writeEntry(featureVector.getValues());
        }
        SvmLearnerAdapter learner = new SvmLearnerAdapter(executionIdentifier);
        learner.execute();
    }

    public void testGeneratedModel(LocalDataAccessFactory daf) {
        DataSetGenerator testSetGenerator = new DataSetGenerator(DataSetType.TEST, executionIdentifier);
        QuestionAnswers questionAnswers = null;
        while (daf.hasNextTest()) {
            questionAnswers = daf.getNextTestEntry();
            FeatureVector featureVector = new FeatureVector(questionAnswers, false);
            featureVector.setFeatures(features);
            featureVector.process();
            testSetGenerator.writeEntry(featureVector.getValues());
        }
        SvmClassifierAdapter classifier = new SvmClassifierAdapter(executionIdentifier);
        classifier.execute();
    }
}
