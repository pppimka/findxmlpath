package agile.test.palii;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ElementFinder {

    private static Logger LOGGER = LogManager.getLogger(ElementFinder.class);
    private static String CHARSET_NAME = "utf8";


    public static Optional<Element> findElementById(File htmlFile, String targetElementId) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath());
            return Optional.ofNullable(doc.getElementById(targetElementId));

        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }

    public static Optional<Element> findElementBySampleButton(File htmlFile, Element buttonOpt) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath());

            Map<Element, Integer> similarElements = new HashMap<>();

            collectSimilarElementsByAttributes(buttonOpt, doc, similarElements);

            incrementElementSimilarityWithSameContent(buttonOpt.text(), similarElements);

            HashMap<Element, Integer> sortedMap = sortMapByValue(similarElements);

            Map.Entry<Element, Integer> theMostSimilarElement = sortedMap.entrySet().iterator().next();
            LOGGER.info("The most similar element: " + theMostSimilarElement.getKey());
            LOGGER.info("Level of similarity: " + theMostSimilarElement.getValue());
            return Optional.of(theMostSimilarElement.getKey());

        } catch (IOException io) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), io);
            return Optional.empty();
        } catch (NoSuchElementException e) {
            LOGGER.error("No similar elements were found in [{}] file", htmlFile.getAbsolutePath());
            return Optional.empty();
        }

    }

    private static void incrementElementSimilarityWithSameContent(String buttonText, Map<Element, Integer> similarElements) {
        similarElements.forEach((k, v) -> {
            if (k.text().equals(buttonText)) {
                similarElements.merge(k, 1, (oldValue, increment) -> oldValue + increment);
            }
        });
    }

    private static void collectSimilarElementsByAttributes(Element buttonOpt, Document doc, Map<Element, Integer> similarElements) {
        buttonOpt.attributes().forEach(
                attribute ->
                        doc.getElementsByAttributeValueMatching(attribute.getKey(), attribute.getValue())
                                .forEach(element ->
                                        similarElements.merge(element, 1, (oldValue, increment) -> oldValue + increment)

                                ));
    }

    private static HashMap<Element, Integer> sortMapByValue(Map<Element, Integer> similarElements) {
        return similarElements.entrySet()
                .stream()
                .sorted(Map.Entry.<Element, Integer>comparingByValue().reversed()).
                        collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

}
