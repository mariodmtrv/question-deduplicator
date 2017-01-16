package edu.fmi.sudo.deduplicator;

import edu.fmi.sudo.deduplicator.entities.OriginalQuestion;
import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;
import edu.fmi.sudo.deduplicator.evaluation.Evaluator;
import edu.fmi.sudo.deduplicator.entities.Thread;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mateev on 15.1.2017 г..
 */
public class EvaluatorTest {
    @Test
    public void assertClassifierOutputPreparation() {
        String pathToPrediction = "." + File.separator + "predictions";
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(pathToPrediction));
            pw.println("0.1");
            pw.println("0.2");
            pw.println("0.3");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }

        Thread t1 = new Thread("oq1_r1", null, null, null);
        Thread t2 = new Thread("oq2_r1", null, null, null);
        Thread t3 = new Thread("oq2_r2", null, null, null);

        OriginalQuestion oq1 = new OriginalQuestion("oq1", "", "");
        OriginalQuestion oq2 = new OriginalQuestion("oq2", "", "");

        QuestionAnswers qa = new QuestionAnswers(oq1, t1);
        QuestionAnswers qa2 = new QuestionAnswers(oq2, new ArrayList<>(Arrays.asList(t2, t3)));

        List<QuestionAnswers> data = new ArrayList<>(Arrays.asList(qa, qa2));

        Evaluator evaluator = new Evaluator();
        evaluator.evaluate(data, pathToPrediction, "android");

        try {
            //List<String> lines = Files.lines(Paths.get(Evaluator.getEvaluationPath() + "android_results.pred")).collect(Collectors.toList());
            List<String> lines = Files.lines(Paths.get("." + File.separator + "android_results.pred")).collect(Collectors.toList());

            assert ("oq1\toq1_r1\t0\t0.1\ttrue".equals(lines.get(0)));
            assert ("oq2\toq2_r1\t0\t0.2\ttrue".equals(lines.get(1)));
            assert ("oq2\toq2_r2\t0\t0.3\ttrue".equals(lines.get(2)));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

