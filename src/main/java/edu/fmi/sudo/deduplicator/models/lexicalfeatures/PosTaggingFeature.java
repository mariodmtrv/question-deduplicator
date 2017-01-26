package edu.fmi.sudo.deduplicator.models.lexicalfeatures;

import edu.fmi.sudo.deduplicator.GateWrapper;
import edu.fmi.sudo.deduplicator.entities.RelatedQuestion;
import edu.fmi.sudo.deduplicator.models.Feature;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.creole.ResourceInstantiationException;
import gate.persist.PersistenceException;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mateev on 19.1.2017 г..
 */
public abstract  class PosTaggingFeature extends Feature {
    private CorpusController application;

    @Override
    public void process() {
        try {
            // initialise GATE - this must be done before calling any GATE APIs
            GateWrapper.init();

            initGateApp();

            // Create a Corpus to use.  We recycle the same Corpus object for each
            // iteration.  The string parameter to newCorpus() is simply the
            // GATE-internal name to use for the corpus.  It has no particular
            // significance.
            Corpus corpus = Factory.newCorpus("SemEval corpus");
            this.application.setCorpus(corpus);

            Document orgQuestionSubjectDoc = Factory.newDocument(
                    this.questionAnswers.getQuestion().getSubject());
            Document orgQuestionBodyDoc = Factory.newDocument(
                    this.questionAnswers.getQuestion().getBody());

            // put the document in the corpus
            corpus.add(orgQuestionSubjectDoc);
            corpus.add(orgQuestionBodyDoc);

            this.application.execute();

            corpus.clear();

            for (RelatedQuestion rq : this.questionAnswers.getAllRelatedQuestions()) {
                Document relQuestionSubjectDoc = Factory.newDocument(
                        rq.getSubject());
                Document relQuestionBodyDoc = Factory.newDocument(
                        rq.getBody());

                corpus.add(relQuestionSubjectDoc);
                corpus.add(relQuestionBodyDoc);

                this.application.execute();

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

    private void initGateApp() throws IOException, PersistenceException, ResourceInstantiationException {
        if (this.application == null) {

            InputStream appStream = getClass().getClassLoader()
                    .getResourceAsStream("modules/gate/gate-pos-app.gapp");

            Files.copy(appStream, Paths.get("./gate-pos-app.gapp"), StandardCopyOption.REPLACE_EXISTING);

            this.application =
                    (CorpusController) PersistenceManager.loadObjectFromFile(
                            new File("./gate-pos-app.gapp"));
        }
    }

    public abstract String processFeatureMetrics(
        Document orgQuestionSubjectDoc,
        Document orgQuestionBodyDoc,
        Document relQuestionSubjectDoc,
        Document relQuestionBodyDoc
    );

    protected String getNormalizedVector(List<Double> values) {
        Double mean = values.stream().mapToDouble(x -> x).average().getAsDouble();
        double diff = values.stream().max(Double::compareTo).get()
                - values.stream().min(Double::compareTo).get();

        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);

        return values.stream()
                .map(v -> diff == 0 ? 0 : (v - mean) / diff)
                .map(v -> df.format(v))
                .collect(Collectors.joining(","));
    }
}
