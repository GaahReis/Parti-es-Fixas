package entities;

public class Partition {

	// Tamanho do processo
    private int pSize;
	// Id do processo
	private int processId;
	// Tamanho da partição
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
	
    public int getPSize() {
        return pSize;
    }

    public void setPSize(int pSize) {
        this.pSize = pSize;
    }
	
}
