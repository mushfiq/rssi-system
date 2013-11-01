package tests;

import java.io.File;

import main.Application;
import data.DataReader;
import data.FileDataReader;

/**
 * The Class ReadingFromFileTest.
 */
public class ReadingFromFileTest {


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		DataReader reader = new FileDataReader(new File("comReader" + File.separator + "main" + File.separator + "resources" + File.separator + "data.txt"));
		reader.readData();
	}

}
