package edu.fmi.sudo.deduplicator.pipeline;

import edu.fmi.sudo.deduplicator.dal.DataAccessFactory;
import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;

import java.util.ArrayList;
import java.util.List;

/**
 * The pipeline's backbone, used to wire the enabled pipeline features
 */
public class MainPipeline {
    private List<PipelineFeature> enabledFeatures;
    private DataAccessFactory daf;

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
}
