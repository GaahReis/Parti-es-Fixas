package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* Montam os processos com base em algumas parametros, co */
public class ProcessBuild extends Thread {

	// Lista de processos

    private final List<Process> processes;
    private int DEFAULT_MAX_PROCESS_SIZE;
    private int NUMBER_OF_PROCESS = 200;
    
    public ProcessBuild(int memorySize) {
        this.processes = new ArrayList<>();
        DEFAULT_MAX_PROCESS_SIZE = memorySize / 10;
    }

    @Override
    public void run(){
        for(int i = 0; i < NUMBER_OF_PROCESS; i++){
            processes.add(new Process(getRandom(DEFAULT_MAX_PROCESS_SIZE), i + 1, getRandom(10)));
        }
    }

    public int getRandom(int upperBound){
        Random random = new Random();

        return random.nextInt(((upperBound + 1) - 1) + 1) + 1;
    }

    public List<Process> getProcess(){
        return this.processes;
    }
}
