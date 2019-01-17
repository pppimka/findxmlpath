package agile.test.palii;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Optional;

import static agile.test.palii.ElementFinder.findElementById;
import static agile.test.palii.ElementFinder.findElementBySampleButton;

public class Main {

    private static Logger LOGGER = LogManager.getLogger(Main.class);

    private static final String DEFAULT_ELEMENT_ID = "make-everything-ok-button";

    public static void main(String[] args) {
        if (args == null || args.length < 2 || args.length > 3) {
            System.out.println("Wrong arguments provided.");
        } else {
            String sampleHtmlPath = args[0];
            String diffCaseHtmlPath = args[1];
            String providedId = null;
            if (args.length == 3) {
                providedId = args[2];
            }

            Optional<Element> buttonOpt = findElementById(new File(sampleHtmlPath), providedId != null ? providedId : DEFAULT_ELEMENT_ID);
            Optional<Element> theMostSimilarElement = findElementBySampleButton(new File(diffCaseHtmlPath), buttonOpt.orElseThrow(NoSuchElementException::new));
            theMostSimilarElement.ifPresent(Main::logPathToElement);

        }
    }

    private static void logPathToElement(Element element) {
        final StringBuilder pathToElement = new StringBuilder();
        element.parents().forEach(e -> pathToElement.insert(0, e.tag() + " -> "));
        pathToElement.append(element.tag());
        pathToElement.insert(0, "Path to found element: ");
        LOGGER.info(pathToElement.toString());
    }
}
