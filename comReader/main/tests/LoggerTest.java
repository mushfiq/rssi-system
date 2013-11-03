package tests;

import org.apache.log4j.Level;

import main.Application;

public class LoggerTest {

	public LoggerTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.getApplication();
		Application.getLogger().log(Level.WARN, "something awkward this way comes");
		Application.getLogger().warn("Something something");
	}

}
