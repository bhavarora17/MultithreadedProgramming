package framework;

public class Output {
	private static Output instance;
	private DinerRecord[] outData;
	
	private Output() {
		
	}
	
	public void init(int numDiners) {
		outData = new DinerRecord[numDiners];
		for(int i=0; i<outData.length; i++) {
			outData[i] = new DinerRecord();
		}
	}
	
	public static Output getInstance() {
		if(instance == null) {
			instance = new Output();
		}
		return instance;
	}

	public DinerRecord[] getOutData() {
		return outData;
	}
}
