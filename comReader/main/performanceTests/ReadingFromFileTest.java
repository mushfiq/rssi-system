package performanceTests;

import java.io.File;

import main.Application;
import data.DataReader;
import data.FileDataReader;

public class ReadingFromFileTest {

	public ReadingFromFileTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		Application application = Application.getApplication();
		DataReader reader = new FileDataReader(new File("comReader" + File.separator + "main" + File.separator + "resources" + File.separator + "data.txt"));
		reader.readData();
	}

}
