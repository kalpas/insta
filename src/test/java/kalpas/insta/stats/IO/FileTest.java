package kalpas.insta.stats.IO;

import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileTest {

    @Test
    public void test1() throws IOException {
        String fileName = "D:\\test321789";
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(fileName + ".gml", false), "UTF-8");
        fileWriter.close();
    }

}
