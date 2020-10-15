package entities;

import java.util.ArrayList;
import java.util.List;

public class Memory extends Thread{

	// Tamanho da memória
	private int memorySize;
	//  Partições presentes na memória
	private List<Partition> partitions;
	
	public Memory(int memorySize) {
		super();
		this.memorySize = memorySize;
	}
	
	public List<Partition> getPartitions() {
		return partitions;
	}
	
	
	// Aloca as partições na memória, cada memória possuindo 10 partições
	@Override
	public void run(){

		final int tamanhoPartition = memorySize / 10;
        partitions = new ArrayList<>();

		for(int i = 0; i < 10; i++){
			partitions.add( new Partition(tamanhoPartition) );
		}

	}
}
