package edu.fmi.sudo.deduplicator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Miroslav Kramolinski
 */
public class XMLValidator {
    private static final String[] TAGS = {
            "OrgQuestion",
            "OrgQSubject",
            "OrgQBody",
            "Thread",
            "RelQuestion",
            "RelQSubject",
            "RelQBody",
            "RelAnswer",
            "RelAText",
            "RelAComment",
            "RelACText"
    };

    /**
     * Perform a validation of the input XML file
     */
    public static boolean validate(String filename) {
        return checkUnbalancedTags(filename);
    }

    private static boolean checkUnbalancedTags(String filename) {
        try(Stream<String> stream = Files.lines(Paths.get(filename))) {
            Integer line = -1;
            Integer[] countOpenTags = new Integer[TAGS.length];
            for(int i = 0; i < TAGS.length; i ++)
                countOpenTags[i] = 0;

            for(String lineStr : stream.collect(Collectors.toList())) {
                line ++;

                for(int i = 0; i < TAGS.length; i ++) {
                    countOpenTags[i] += lineStr.split("<" + TAGS[i]).length;
                    countOpenTags[i] -= lineStr.split("</" + TAGS[i]).length;

                    if (countOpenTags[i] >= 2) {
                        System.out.println("ERROR: Second opening tag <" + TAGS[i] + "> located before a closing </" + TAGS[i] + "> at line " + line);
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("INFO: Validation of tag balance successful");

        return true;
    }
}
