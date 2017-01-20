package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.GateWrapper;
import edu.fmi.sudo.deduplicator.entities.RelatedQuestion;
import edu.fmi.sudo.deduplicator.models.Feature;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by mateev on 19.1.2017 г..
 */
public abstract  class PosTaggingFeature extends Feature {
    @Override
    public void process() {
        try {
            // initialise GATE - this must be done before calling any GATE APIs
            GateWrapper.init();

            // load the saved application
            String appPath = "src\\main\\resources\\modules\\gate\\gate-pos-app.gapp";
            CorpusController application =
                    (CorpusController) PersistenceManager.loadObjectFromFile(
                            new File(appPath));

            // Create a Corpus to use.  We recycle the same Corpus object for each
            // iteration.  The string parameter to newCorpus() is simply the
            // GATE-internal name to use for the corpus.  It has no particular
            // significance.
            Corpus corpus = Factory.newCorpus("SemEval corpus");
            application.setCorpus(corpus);

            Document orgQuestionSubjectDoc = Factory.newDocument(
                    this.questionAnswers.getQuestion().getSubject());
            Document orgQuestionBodyDoc = Factory.newDocument(
                    this.questionAnswers.getQuestion().getBody());

            // put the document in the corpus
            corpus.add(orgQuestionSubjectDoc);
            corpus.add(orgQuestionBodyDoc);

            application.execute();

            corpus.clear();

            for (RelatedQuestion rq : this.questionAnswers.getAllRelatedQuestions()) {
                Document relQuestionSubjectDoc = Factory.newDocument(
                        rq.getSubject());
                Document relQuestionBodyDoc = Factory.newDocument(
                        rq.getBody());

                corpus.add(relQuestionSubjectDoc);
                corpus.add(relQuestionBodyDoc);

                application.execute();

                //Process annotations
                String vector = processFeatureMetrics(
                        orgQuestionSubjectDoc,
                        orgQuestionBodyDoc,
                        relQuestionSubjectDoc,
                        relQuestionBodyDoc);

                this.featureValue.add(vector);

                corpus.clear();

                Factory.deleteResource(relQuestionBodyDoc);
                Factory.deleteResource(relQuestionSubjectDoc);
            }

            Factory.deleteResource(orgQuestionBodyDoc);
            Factory.deleteResource(orgQuestionSubjectDoc);
        } catch (GateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract String processFeatureMetrics(
        Document orgQuestionSubjectDoc,
        Document orgQuestionBodyDoc,
        Document relQuestionSubjectDoc,
        Document relQuestionBodyDoc
    );
}
