/**
 * Used to evaluate the quality of created models
 *
 * @author Georgi Mateev
 */
package edu.fmi.sudo.deduplicator.evaluation;

import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;
import edu.fmi.sudo.deduplicator.entities.Thread;

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

    public void evaluate(List<QuestionAnswers> data, String pathToClassifiedData, String category) {
        prepareClassifierOutput(data, pathToClassifiedData, category);

//        ClassLoader classLoader = this.getClass().getClassLoader();
//         Getting resource(File) from class loader
//        File configFile=new File(classLoader.getResource(fileName).getFile());
//
//        Read more at http://www.java2blog.com/2016/02/read-file-from-resources-folder-in-java.html#D4WuwSZ5CXBC5Jcp.99
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

    public void prepareClassifierOutput(List<QuestionAnswers> data, String pathToClassifiedData, String category) {
        try {
            List<String> lines = Files.lines(Paths.get(pathToClassifiedData)).collect(Collectors.toList());

            //PrintWriter pw = new PrintWriter(new FileWriter(this.evaluatorPath + category + "_results.pred", true));
            PrintWriter pw = new PrintWriter(new FileWriter("." + File.separator + category + "_results.pred"));

            int counter = 0;
            for (QuestionAnswers qa: data) {
                for (Thread t: qa.getThreads()) {
                    String p;
                    if(counter >= lines.size()) {
                        p = "0";
                    } else {
                        p = lines.get(counter);
                    }
                    String line = String.format("%s\t%s\t0\t%s\t%s", qa.getQuestion().getId(), t.getId(), p, true);
                    pw.println(line);

                    counter++;
                }
            }

            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
