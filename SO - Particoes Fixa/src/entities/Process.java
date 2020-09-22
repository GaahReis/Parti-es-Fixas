package entities;

public class Process {

	private int size;
	private int id;
	private int time;
	
	public Process(int size, int id, int time) {
		super();
		this.size = size;
		this.id = id;
		this.time = time;
	}

	public int getSize() {
		return size;
	}

	public int getId() {
		return id;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	
	/* public void run() {
		
	} */
	
}
