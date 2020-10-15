package simuladormemoriafixas.Process;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProcessMaker extends Thread {

    private final List<Process> processes;
    private int DEFAULT_MAX_PROCESS_SIZE;
    private final int DEFAULT_MAX_CPU_TIME = 10;

    public ProcessMaker(int memorySize) {
        this.processes = new ArrayList<>();
        DEFAULT_MAX_PROCESS_SIZE = memorySize / 10;
    }


    @Override
    public void run(){
        for(int id = 0; id < 200; id++){
            processes.add(new Process(id + 1, getRandonNumber(DEFAULT_MAX_PROCESS_SIZE), getRandonNumber(DEFAULT_MAX_CPU_TIME)));
        }
    }

    public int getRandonNumber(int upperBound){
        Random random = new Random();

        return random.nextInt(((upperBound + 1) - 1) + 1) + 1;
    }

    public List<Process> getProcess(){
        return this.processes;
    }
}
