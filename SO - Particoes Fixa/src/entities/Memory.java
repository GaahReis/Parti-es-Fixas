package entities;

import java.util.List;

public class Memory {

	private int memorySize;
	private List<Partition> partitions;
	
	public Memory(int memorySize) {
		super();
		this.memorySize = memorySize;
	}
	
	public List<Partition> getPartitions() {
		return partitions;
	}
	
	/* public void run() {
		
	} */
}
