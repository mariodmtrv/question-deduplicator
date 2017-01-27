package edu.fmi.sudo.deduplicator.models;

import edu.fmi.sudo.deduplicator.entities.*;
import edu.fmi.sudo.deduplicator.entities.Thread;
import edu.fmi.sudo.deduplicator.models.lexicalfeatures.*;
import edu.fmi.sudo.deduplicator.pipeline.TokenizationFilter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FeatureVectorTest {
    private QuestionAnswers qa;
    private List<Feature> features =
            Collections.unmodifiableList(
                    Arrays.asList(
                            new BiGramsFeature()
                            , new CommonTagsFeature()
                            , new CosSimilarityFeature()
                            , new MatchingWordsFeature()
                            , new PosTaggingNounOverlapFeature()
                            , new PosTaggingProportionsFeature()
                            , new UserVotesFeature()
                    ));

    @Before
    public void setUp() {
        OriginalQuestion oq1 = new OriginalQuestion("oq1",
                "Which is the best car in the whole world",
                "Thinking of Aston Martin Vanquish, Porsche Panamera, Ferrari Spyder ...");

        RelatedQuestion rq1 = new RelatedQuestion("rq1_a1",Relevance.Related);
        rq1.setSubject("Which is the most reliable car in the whole world");
        rq1.setBody("Heard that Aston Martin Vanquish and Porsche Panamera break quickly, but Ferrari Spyder ...");
        rq1.setTags(Arrays.asList("car", "world", "reliable"));
        RelatedQuestion rq2 = new RelatedQuestion("rq2_a2",Relevance.PerfectMatch);
        rq2.setSubject("Which is the best value sports car in the whole world");
        rq2.setBody("Heard that Aston Martin Vanquish and Porsche Panamera are overpriced, but Ferrari Spider ...");
        rq2.setTags(Arrays.asList("overpiced", "sports", "car", "value"));

        List<RelatedAnswer> answers = new ArrayList<RelatedAnswer>(Arrays.asList(new RelatedAnswer(null,null,null,null,1,true,"Aston is awesome",new ArrayList<RelatedComment>(Arrays.asList(new RelatedComment(null,null,null,null,1,"Aston")))))) ;
        Thread t1 = new Thread("oq1_r1", rq1, answers, null);
        Thread t2 = new Thread("oq1_r2", rq2, answers, null);
        this.qa =  new QuestionAnswers(oq1, Arrays.asList(t1, t2));
        TokenizationFilter tf = new TokenizationFilter();
        this.qa = tf.process(qa);
    }

    @Test
    public void testMatrixGeneration() {
        FeatureVector featureVector = new FeatureVector(qa, true, 1);
        featureVector.setFeatures(features);
        featureVector.getValues();
    }
}
