package edu.fmi.sudo.deduplicator.models.evaluationfeatures;

import edu.fmi.sudo.deduplicator.entities.*;
import edu.fmi.sudo.deduplicator.entities.Thread;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mateev on 24.1.2017 г..
 */
public class VectorMetadataFeatureTest {
    @Test
    public void testMetadata() {
        RelatedQuestion r1 = new RelatedQuestion("oq1_R1", Relevance.PerfectMatch);


        RelatedQuestion r2 = new RelatedQuestion("oq1_R2", Relevance.Related);

        Thread t1 = new Thread("oq1_r1", r1, null, null);
        Thread t2 = new Thread("oq1_r2", r2, null, null);

        OriginalQuestion oq1 = new OriginalQuestion("oq1",
                "Receipt for cooking a pie.",
                "I don't have a receipt for a pie so can you give me one?");

        QuestionAnswers qa = new QuestionAnswers(oq1, Arrays.asList(t1, t2));

        VectorMetadataFeature f = new VectorMetadataFeature();
        f.setQuestionAnswers(qa);
        List<String> vector = f.toVector();

        assert (vector.get(0).equals("#oq1 1 PerfectMatch"));
        assert (vector.get(1).equals("#oq1 2 Related"));
    }
}
