package simuladormemoriafixas.MainApplication;

import simuladormemoriafixas.Manager.Alocador;
import simuladormemoriafixas.Memory.Memory;
import simuladormemoriafixas.Process.ProcessMaker;

public class SimuladorMemoriaFixasSwap {

    private static final int MEMORY_SIZE = 1024;

    public static void main(String[] args) {
        Memory memory = new Memory(MEMORY_SIZE);
        ProcessMaker processMaker = new ProcessMaker(MEMORY_SIZE);

        try {

            processMaker.run();
            processMaker.join();

            memory.run();
            memory.join();

            Alocador alocador = new Alocador(memory, processMaker.getProcess());
            alocador.run();
            alocador.join();

            alocador.printFinalRelatorio();
            alocador.exportLog();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


