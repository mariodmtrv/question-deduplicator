package edu.fmi.sudo.deduplicator.pipeline;

public enum PipelinePreProcessTask {
    HTML_UNESCAPE,
    GENERAL_STOPWORDS_REMOVAL,
    SPECIALIZED_STOPWORDS_REMOVAL,
    POS_TAGGING,
    WSD,
    MAGIC_WORDS,
    TOKENIZATION
}
