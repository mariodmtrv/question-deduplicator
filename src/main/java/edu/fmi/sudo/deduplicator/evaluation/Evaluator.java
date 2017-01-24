/**
 * Used to evaluate the quality of created models
 *
 * @author Georgi Mateev
 */
package edu.fmi.sudo.deduplicator.evaluation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Evaluator {

    private static String evaluatorPath = "src\\main\\resources\\modules\\evaluation\\";
    private Map<String, String> categoriesMap = new HashMap<String, String>();

    public static String getEvaluationPath () {
        return evaluatorPath;
    }

    public Evaluator() {
        this.categoriesMap.put("android", "stackexchange_android_devel.xml.subtaskE.relevancy.bm25");
        this.categoriesMap.put("english", "stackexchange_english_devel.xml.subtaskE.relevancy.bm25");
        this.categoriesMap.put("gaming", "stackexchange_gaming_devel.xml.subtaskE.relevancy.bm25");
        this.categoriesMap.put("wordpress", "stackexchange_wordpress_devel.xml.subtaskE.relevancy.bm25");
    }

    public void evaluate(String pathToTestData, String pathToClassifiedData, String category) {
        prepareClassifierOutput(pathToTestData, pathToClassifiedData, category);

        String command = String.format("python %1$sev.py %1$s%2$s .\\%3$s_results.pred",
                this.evaluatorPath,
                this.categoriesMap.get(category),
                category);
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prepareClassifierOutput(String pathToTestData, String pathToClassifiedData, String category) {
        try {
            List<String> testLines = Files.lines(Paths.get(pathToTestData)).collect(Collectors.toList());
            List<String> lines = Files.lines(Paths.get(pathToClassifiedData)).collect(Collectors.toList());

            PrintWriter pw = new PrintWriter(new FileWriter("." + File.separator + category + "_results.pred"));

            String previousOrigQId = "";

            for (int i = 0; i < testLines.size(); i++) {
                String testLine = testLines.get(i);
                String prediction = lines.get(i);

                String[] info = testLine.split("#")[1].split(" ");

                if (!previousOrigQId.equals("") && !previousOrigQId.equals(info[0])) {
                    String nilLine = String.format("%s\tNIL",
                            previousOrigQId);

                    pw.println(nilLine);
                }

                boolean perfectMatch = "PerfectMatch".equals(info[2]);

                String evalLine = String.format("%1$s\t%1$s_R%2$s\t0\t%3$s\t%4$s",
                        info[0],
                        info[1],
                        prediction,
                        perfectMatch);

                pw.println(evalLine);

                previousOrigQId = info[0];
            }

            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
