package main;

import java.io.File;

public class Application {

	private static Application application;
	private File configurationFile;
	
	
	private Application(){
		
		readConfigurationFile();
		initializeGUI();
	}
	
	private void initializeGUI() {
		// TODO Auto-generated method stub
		
	}

	private void readConfigurationFile() {
		// TODO Auto-generated method stub
		
	}

	public static Application getApplication(){
		
		if(application == null){
			application = new Application();
		}
		
		return application;
	}
	
	
}
