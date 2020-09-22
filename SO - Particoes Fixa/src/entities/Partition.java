package entities;

public class Partition {

	private int processId;
	private int size;
	
	public Partition(int size) {
		super();
		this.size = size;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public int getSize() {
		return size;
	}	
	
}
