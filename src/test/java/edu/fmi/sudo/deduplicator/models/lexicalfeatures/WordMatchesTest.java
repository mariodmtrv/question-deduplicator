package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.entities.OriginalQuestion;
import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;
import edu.fmi.sudo.deduplicator.entities.RelatedQuestion;
import edu.fmi.sudo.deduplicator.entities.Thread;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class WordMatchesTest {
    private QuestionAnswers qa;
    @Before
    public void setUp(){

        OriginalQuestion oq1 = new OriginalQuestion("oq1",
                "Which is the best car in the whole world",
                "Thinking of Aston Martin Vanquish, Porsche Panamera, Ferrari Spyder ...");

        RelatedQuestion rq1 = new RelatedQuestion();
        rq1.setSubject("Which is the most reliable car in the whole world");
        rq1.setBody("Heard that Aston Martin Vanquish and Porsche Panamera break quickly, but Ferrari Spyder ...");
        RelatedQuestion rq2 = new RelatedQuestion();
        rq2.setSubject("Which is the best value sports car in the whole world");
        rq2.setBody("Heard that Aston Martin Vanquish and Porsche Panamera are overpriced, but Ferrari Spider ...");

        Thread t1 = new Thread("oq1_r1", rq1, null, null);
        Thread t2 = new Thread("oq1_r2", rq2, null, null);
       this.qa =  new QuestionAnswers(oq1, Arrays.asList(t1, t2));
    }

    @Test
    public void testMatchingWordsFeature() {
        MatchingWordsFeature wordsFeature = new MatchingWordsFeature();
        wordsFeature.setQuestionAnswers(qa);
        wordsFeature.process();
        wordsFeature.toVector();
    }

    @Test
    public void testBiGramsFeature() {
        BiGramsFeature wordsFeature = new BiGramsFeature();
        wordsFeature.setQuestionAnswers(qa);
        wordsFeature.process();
        wordsFeature.toVector();
    }

    @Test
    public void testIntersectionFinder() {
        IntersectionFinder finder
                = new IntersectionFinder(2, false);
        finder.setSourceEntityWords("This is a red car");
        Double size = finder
                .getIntersectionSize("That is a red car too");
        assert (size - 2.0 < 0.001);
    }
}
