package edu.fmi.sudo.deduplicator.pipeline;

import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;

public class PipelineFilter {
    public static QuestionAnswers process(PipelineFeature feature, QuestionAnswers qa) {
        switch(feature) {
            case GENERAL_STOPWORDS_REMOVAL:
                return GeneralStopwordsRemovalFilter.process(qa);
            case SPECIALIZED_STOPWORDS_REMOVAL:
                return SpecializedStopwordsRemovalFilter.process(qa);
            case POS_TAGGING:
                return POSTaggingFilter.process(qa);
            case WSD:
                return WSDFilter.process(qa);
            case MAGIC_WORDS:
                return MagicWordsFilter.process(qa);
            default:
                throw new RuntimeException("Unrecognized feature");
        }
    }
}
