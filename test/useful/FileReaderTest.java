package useful;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by junior on 8/16/15.
 */
public class FileReaderTest {

    private static final String FILE_FOR_TEST = "fileForTest";
    private TestFileHandler testScenarioHandler;
    private TestableFileReader fileReader;

    @Before
    public void setUp() throws Exception {
        testScenarioHandler = new TestFileHandler(FILE_FOR_TEST);
    }

    @After
    public void tearDown() {
        fileReader.closeFile();
        testScenarioHandler.deleteFile();
    }

    @Test public void
    fileNotExists_createError() throws Exception {
        fileReader = new TestableFileReader("fileNotExists");
        assertThat(fileReader.isError(), is(true));
    }

    @Test public void
    openEmptyFile_extractEmptyString() throws Exception {
        testScenarioHandler.createEmptyFile();

        fileReader = new TestableFileReader(FILE_FOR_TEST);
        assertThat(fileReader.getLine(), is(""));
    }

    @Test public void
    openFile_extractItsLines() throws Exception {
        testScenarioHandler.createFileWith("Little text here\nwith two lines");

        fileReader = new TestableFileReader(FILE_FOR_TEST);
        assertThat(fileReader.getLine(), is("Little text here"));
        assertThat(fileReader.getLine(), is("with two lines"));
        assertThat(fileReader.getLine(), is(""));
    }
}

class TestableFileReader extends FileReader {
    private boolean error;

    public TestableFileReader(String fileName) {
        super(fileName);
    }

    @Override
    protected void fileNotFoundHandler(String fileName) {
        error = true;
    }

    public boolean isError() { return error; }
}

class TestFileHandler {
    private File file;

    public TestFileHandler(String name) {
        file = new File(name);
    }

    public void createEmptyFile() {
        try {
            file.createNewFile();
        } catch (Exception e) {};
    }

    public void createFileWith(String text) {
        createEmptyFile();

        try {
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(file.getAbsoluteFile()));
            bw.write(text);
            bw.close();
        } catch (Exception e) {};
    }

    public void deleteFile() {
        try {
            file.delete();
        } catch (Exception e) {};
    }
}