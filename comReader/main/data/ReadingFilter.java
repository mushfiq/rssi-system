package data;

public class ReadingFilter {

	private Reading data;
	
	public ReadingFilter() {
		// TODO Auto-generated constructor stub
	}
	
	public ReadingFilter(Reading data) {
		super();
		if(data == null){
			this.data = new Reading();
		}
		else{
			this.data = data;
		}
		
	}
	
	public void removeInappropriateValues(){
		
	}
	
	public int calculateAverage(){
		
		return 0;
	}
	
	public Reading getData() {
		return data;
	}

	public void setData(Reading data) {
		this.data = data;
	}

	
}
