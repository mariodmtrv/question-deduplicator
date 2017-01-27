package edu.fmi.sudo.deduplicator.evaluation;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        String pathToTest = "." + File.separator + "tests";
        PrintWriter pwTest = null;
        try {
            pwTest = new PrintWriter(new FileWriter(pathToTest));
            pwTest.println("3 qid:1 1:1 2:1 3:0 4:0.2 5:0 #1 1 PerfectMatch");
            pwTest.println("3 qid:1 1:1 2:1 3:0 4:0.2 5:0 #1 2 Related");
            pwTest.println("3 qid:1 1:1 2:1 3:0 4:0.2 5:0 #2 1 Irrelevant");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pwTest != null) {
                pwTest.close();
            }
        }

        Evaluator evaluator = new Evaluator();
        evaluator.evaluate(pathToTest, pathToPrediction, "android");

        try {
            //List<String> lines = Files.lines(Paths.get(Evaluator.getEvaluationPath() + "android_results.pred")).collect(Collectors.toList());
            List<String> lines = Files.lines(Paths.get("." + File.separator + "android_results.pred")).collect(Collectors.toList());

            assert ("1\t1_R1\t0\t0.1\ttrue".equals(lines.get(0)));
            assert ("1\t1_R2\t0\t0.2\tfalse".equals(lines.get(1)));
            assert ("1\tNIL".equals(lines.get(2)));
            assert ("2\t2_R1\t0\t0.3\tfalse".equals(lines.get(3)));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

