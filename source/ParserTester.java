import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import javax.swing.plaf.SliderUI;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ParserTester {
	
	public File test = new File("src/test.txt");
	public File myClass = new File("src/TestClass.java");
	public String filePath = test.getAbsolutePath();
	public String myClassPath = myClass.getAbsolutePath();


	@Test
	public void testReadFileToString() throws IOException {
		assertEquals("This is a test text.", SimpleParser.readFileToString(filePath));
	}
	
	@Test
	public void testParse() throws IOException{
		//Method that I found to test the output in the console
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
		SimpleParser.parse(myClass, SimpleParser.readFileToString(myClassPath));
        baos.flush();
        String printed = new String(baos.toByteArray());
        String[] linesOfOutput = printed.split(System.getProperty("line.separator"));
        
        //comparing the output
        assertEquals("TestClass.java | 13 | sayHello()", linesOfOutput[0]);
	}
}