package main;

public class Application {

	private static Application application;
	
	private Application(){
		
	}
	
	public static Application getApplication(){
		
		if(application == null){
			application = new Application();
		}
		
		return application;
	}
}
