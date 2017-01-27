package edu.fmi.sudo.deduplicator.pipeline;

import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;

public class PipelinePreProcessor {
    public static QuestionAnswers process(PipelinePreProcessTask filter, QuestionAnswers qa) {
        switch(filter) {
            case HTML_UNESCAPE:
                return HtmlUnescapeFilter.process(qa);
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
            case TOKENIZATION:
                return TokenizationFilter.process(qa);
            default:
                throw new RuntimeException("Unrecognized feature");
        }
    }
}
