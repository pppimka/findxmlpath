package agile.test.palii;


import org.junit.Test;

import java.util.NoSuchElementException;

public class MainTest {

    @Test
    public void testMainSample1() {
        String[] args = {"src/test/resources/sample-0-origin.html", "src/test/resources/sample-1-evil-gemini.html"};
        Main.main(args);
    }

    @Test
    public void testMainSample2() {
        String[] args = {"src/test/resources/sample-0-origin.html", "src/test/resources/sample-2-container-and-clone.html"};
        Main.main(args);
    }

    @Test
    public void testMainSample3() {
        String[] args = {"src/test/resources/sample-0-origin.html", "src/test/resources/sample-3-the-escape.html"};
        Main.main(args);
    }
    @Test
    public void testMainSample4() {
        String[] args = {"src/test/resources/sample-0-origin.html", "src/test/resources/sample-4-the-mash.html"};
        Main.main(args);
    }

    @Test(expected = NoSuchElementException.class)
    public void testMainWrongId() {
        String[] args = {"src/test/resources/sample-0-origin.html", "src/test/resources/sample-2-container-and-clone.html", "wrongId"};
        Main.main(args);
    }
}
