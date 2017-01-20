package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.GateWrapper;
import edu.fmi.sudo.deduplicator.entities.OriginalQuestion;
import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;
import edu.fmi.sudo.deduplicator.entities.RelatedQuestion;
import edu.fmi.sudo.deduplicator.entities.Thread;
import gate.util.GateException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mateev on 18.1.2017 г..
 */
public class PosTaggingFeatureTest {

    @Test
    public void testPosTaggingNounOverlap() {

        RelatedQuestion r1 = new RelatedQuestion();
        r1.setSubject("How to cook a pie?");
        r1.setBody("Can someone tell me how to cook a big pie?");

        RelatedQuestion r2 = new RelatedQuestion();
        r2.setSubject("Need help for choosing a car.");
        r2.setBody("I don't have experience and I need a car maniac to choose car for me. I'll bake him a pie.");

        Thread t1 = new Thread("oq1_r1", r1, null, null);
        Thread t2 = new Thread("oq1_r2", r2, null, null);

        OriginalQuestion oq1 = new OriginalQuestion("oq1",
                "Receipt for cooking a pie.",
                "I don't have a receipt for a pie so can you give me one?");

        QuestionAnswers qa = new QuestionAnswers(oq1, Arrays.asList(t1, t2));

        PosTaggingNounOverlapFeature feature = new PosTaggingNounOverlapFeature();
        feature.setQuestionAnswers(qa);

        feature.toVector();
    }
}
