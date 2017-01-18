package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.GateWrapper;
import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;
import edu.fmi.sudo.deduplicator.models.Feature;
import gate.*;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by mateev on 18.1.2017 г..
 */
public class PosTaggingProportionsFeature extends Feature {
    @Override
    public void process() {
        try {
            // initialise GATE - this must be done before calling any GATE APIs
            GateWrapper.init();

            // load the saved application
            CorpusController application =
                (CorpusController) PersistenceManager.loadObjectFromFile(
                        new File("src\\main\\resources\\modules\\gate\\gate-pos-app.gapp"));

            // Create a Corpus to use.  We recycle the same Corpus object for each
            // iteration.  The string parameter to newCorpus() is simply the
            // GATE-internal name to use for the corpus.  It has no particular
            // significance.
            Corpus corpus = Factory.newCorpus("SemEval corpus");
            application.setCorpus(corpus);

            Document orgQuestionSubjectDoc = Factory.newDocument(this.questionAnswers.getQuestion().getSubject());
            Document orgQuestionBodyDoc = Factory.newDocument(this.questionAnswers.getQuestion().getBody());

            Document relQuestionSubjectDoc = Factory.newDocument(this.questionAnswers.getQuestion().getSubject());
            Document relQuestionBodyDoc = Factory.newDocument(this.questionAnswers.getQuestion().getBody());


            // put the document in the corpus
            corpus.add(orgQuestionSubjectDoc);
            corpus.add(orgQuestionBodyDoc);
            corpus.add(relQuestionSubjectDoc);
            corpus.add(relQuestionBodyDoc);

            // run the application
            application.execute();

            //Process annotations

            // remove the document from the corpus again
            corpus.clear();

            Factory.deleteResource(orgQuestionBodyDoc);
            Factory.deleteResource(orgQuestionSubjectDoc);
            Factory.deleteResource(relQuestionBodyDoc);
            Factory.deleteResource(relQuestionSubjectDoc);

        } catch (GateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
