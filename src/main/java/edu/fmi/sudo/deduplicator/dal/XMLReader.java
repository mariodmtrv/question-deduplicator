package edu.fmi.sudo.deduplicator.dal;

import edu.fmi.sudo.deduplicator.entities.*;
import edu.fmi.sudo.deduplicator.entities.Thread;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Miroslav Kramolinski
 */
public class XMLReader {
    public static List<QuestionAnswers> readFile(String filename) {
        Map<String, QuestionAnswers> questionAnswers = new HashMap<>(); // Pair of ORGQ_ID and the QuestionAnswer object

        try {
            File input = new File(filename);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(input);
            NodeList originalQuestions = document.getElementsByTagName("OrgQuestion");

            for(int i = 0; i < originalQuestions.getLength(); i ++) {
                Element element = (Element) originalQuestions.item(i);

                OriginalQuestion originalQuestion = new OriginalQuestion(
                    element.getAttribute("ORGQ_ID"),
                    element.getElementsByTagName("OrgQSubject").item(0).getTextContent(),
                    element.getElementsByTagName("OrgQBody").item(0).getTextContent()
                );

                Element threadElement = (Element) element.getElementsByTagName("Thread").item(0);
                Thread thread = new Thread(
                    threadElement.getAttribute("THREAD_SEQUENCE"),
                    getRelatedQuestionData(threadElement),
                    getRelatedAnswersData(threadElement),
                    getRelatedCommentsData(threadElement, false)
                );

                if(questionAnswers.containsKey(originalQuestion.getId()))
                    questionAnswers.get(originalQuestion.getId()).addThread(thread);
                else
                    questionAnswers.put(originalQuestion.getId(), new QuestionAnswers(originalQuestion, thread));
            }
        } catch (ParserConfigurationException|IOException|SAXException e) {
            e.printStackTrace();
        }

        return new ArrayList<QuestionAnswers>(questionAnswers.values());
    }

    private static RelatedQuestion getRelatedQuestionData(Element thread) {
        Element relatedQuestion = (Element) thread.getElementsByTagName("RelQuestion").item(0);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return new RelatedQuestion(
                relatedQuestion.getAttribute("RELQ_ID"),
                relatedQuestion.getAttribute("RELQ_CATEGORY"),
                df.parse(relatedQuestion.getAttribute("RELQ_DATE")),
                Integer.parseInt(relatedQuestion.getAttribute("RELQ_RANKING_ORDER")),
                Integer.parseInt(relatedQuestion.getAttribute("RELQ_SCORE")),
                Integer.parseInt(relatedQuestion.getAttribute("RELQ_VIEWCOUNT")),
                Relevance.valueOf(relatedQuestion.getAttribute("RELQ_RELEVANCE2ORGQ")),
                Arrays.asList(relatedQuestion.getAttribute("RELQ_TAGS").split(",")),
                Integer.parseInt(relatedQuestion.getAttribute("RELQ_USERID")),
                relatedQuestion.getAttribute("RELQ_USERNAME"),
                relatedQuestion.getElementsByTagName("RelQSubject").item(0).getTextContent(),
                relatedQuestion.getElementsByTagName("RelQBody").item(0).getTextContent()
            );
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<RelatedAnswer> getRelatedAnswersData(Element thread) {
        List<RelatedAnswer> relatedAnswers = new ArrayList<>();
        NodeList relatedAnswersNodes = thread.getElementsByTagName("RelAnswer");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for(int i = 0; i < relatedAnswersNodes.getLength(); i ++) {
            Element element = (Element) relatedAnswersNodes.item(i);
            try {
                relatedAnswers.add(new RelatedAnswer(
                    element.getAttribute("RELA_ID"),
                    df.parse(element.getAttribute("RELA_DATE")),
                    Integer.parseInt(element.getAttribute("RELA_USERID")),
                    element.getAttribute("RELA_USERNAME"),
                    Integer.parseInt(element.getAttribute("RELA_SCORE")),
                    Boolean.parseBoolean(element.getAttribute("RELA_ACCEPTED")),
                    element.getElementsByTagName("RelAText").item(0).getTextContent(),
                    getRelatedCommentsData(element, true)
                ));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return relatedAnswers;
    }

    private static List<RelatedComment> getRelatedCommentsData(Element thread, boolean forAnswer) {
        List<RelatedComment> relatedComments = new ArrayList<>();
        NodeList relatedCommentsNodes = thread.getElementsByTagName(forAnswer? "RelAComment": "RelComment");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for(int i = 0; i < relatedCommentsNodes.getLength(); i ++) {
            Element element = (Element) relatedCommentsNodes.item(i);

            try {
                relatedComments.add(new RelatedComment(
                    element.getAttribute(forAnswer? "RELAC_ID": "RELC_ID"),
                    df.parse(element.getAttribute(forAnswer? "RELAC_DATE": "RELC_DATE")),
                    Integer.parseInt(element.getAttribute(forAnswer? "RELAC_USERID": "RELC_USERID")),
                    element.getAttribute(forAnswer? "RELAC_USERNAME": "RELC_USERNAME"),
                    Integer.parseInt(element.getAttribute(forAnswer? "RELAC_SCORE": "RELC_SCORE")),
                    element.getElementsByTagName(forAnswer? "RelACText": "RelCText").item(0).getTextContent()
                ));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return relatedComments;
    }
}
