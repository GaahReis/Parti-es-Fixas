package entities;

public class Process {

	// Tamanho processo
	private int size;
	// Id processo
	private int id;
	// Tempo
	private int time;
	// Saber se processo está sendo executado
    private boolean executing;
	
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

	public boolean isExecuting() {
		return executing;
	}

	public void setExecuting(boolean executing) {
		this.executing = executing;
	}
	

    
	
	
	/* public void run() {
		
	} */
	
}
