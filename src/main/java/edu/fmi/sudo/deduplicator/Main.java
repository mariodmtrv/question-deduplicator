package edu.fmi.sudo.deduplicator;

import edu.fmi.sudo.deduplicator.dal.XMLReader;
import edu.fmi.sudo.deduplicator.entities.QuestionAnswers;

import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String devData =
                args.length > 0?
                        args[0]:
                        "M:\\FMI\\SemEval\\DevData\\dev_set.txt";

        System.out.println("Input file set to: " + devData);
        List<QuestionAnswers> data = XMLReader.readFile(Paths.get(devData).toString());
        for(QuestionAnswers qa: data) {
            System.out.println(qa.getQuestion().getId());
        }
    }
}
