package simuladormemoriafixas.Manager;


import simuladormemoriafixas.Memory.Memory;
import simuladormemoriafixas.Memory.Partition;
import simuladormemoriafixas.Process.Process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class Alocador extends Thread{

	private Memory memory;
	private List<Process> processes;
	private int fragmented;
	private int executed;
	private int notExecuted;
	private int timeExecution;
	private List<String> logExport;

	public Alocador(Memory memory, List<Process> processes)
	{
	    this.memory = memory;
	    this.processes = processes;
	    this.logExport = new ArrayList<>();
	}
	
	@Override
	public void run()
	{
        Iterator<Process> iteratorProcess = processes.iterator();

        while(iteratorProcess.hasNext()) {

            Process process = iteratorProcess.next();
            logExport.add("Vou executar o processo de pid: " + process.getPid());
            System.out.println("Vou executar o processo de pid: " + process.getPid());

            Iterator<Partition> iterator = memory.getPartitions().iterator();

            while (iterator.hasNext()) {

                Partition partition = iterator.next();

                if (partition.getPidProcess() == 0) {

                    partition.setPidProcess(process.getPid());
                    partition.setProcessSizeInPartition(process.getSize());

                    synchronized (process) {
                        process.setUpruning(true);
                        executed++;
                    }

                    if (process.getSize() < partition.getSize()) {
                        logExport.add("Houve fragmentacao interna");
                        System.out.println("Houve fragmentacao interna");
                        fragmented++;
                        break;
                    }

                } else {
                    logExport.add("Partition ocupada! Vou para a proxima partition");
                    System.out.println("Partition ocupada! Vou para a proxima partition");

                    Process processExecuting = processes.get(partition.getPidProcess() - 1);

                    synchronized (partition) {
                        synchronized (processExecuting) {

                            int cpuTime = processExecuting.getCpuTime();
                            processExecuting.setCpuTime(--cpuTime);
                            logExport.add("Decrementando cpu time do processo: " + processExecuting.getPid() + " Tempo para finalizar, " + processExecuting.getCpuTime() + " Unidades de CPU");
                            System.out.println("Decrementando cpu time do processo: " + processExecuting.getPid() + " Tempo para finalizar, " + processExecuting.getCpuTime() + " Unidades de CPU");
                        }
                    }

                    if (processExecuting.getCpuTime() <= 0) {
                        logExport.add("Processo " + processExecuting.getPid() + " Terminou! ");
                        System.out.println("Processo " + processExecuting.getPid() + " Terminou! ");

                        synchronized (partition) {
                            synchronized (processExecuting) {
                                processExecuting.setUpruning(false);
                                logExport.add("Vou aproveitar e colocar esse outro processo! Colocando processo: " + process.getPid());
                                System.out.println("Vou aproveitar e colocar esse outro processo! Colocando processo: " + process.getPid());
                                executed++;
                            }
                        }

                        partition.setPidProcess(process.getPid());
                        partition.setProcessSizeInPartition(process.getSize());

                        synchronized (process) {
                            process.setUpruning(true);
                        }

                        if (process.getSize() < partition.getSize()) {
                            logExport.add("Houve fragmentacao externa");
                            System.out.println("Houve fragmentacao externa");
                            fragmented++;
                        }

                        break;
                    }
                }

                stopCPU();

            }

            timeExecution++;

            if (!process.isUpruning()) {
                logExport.add("Não foi possível encontrar partitions, Houve uma fragmentacao!");
                System.out.println("Não foi possível encontrar partitions, Houve uma fragmentacao!");
                notExecuted++;
            }
        }

        addRemainingTime();

	}

    private void addRemainingTime() {

	    Iterator<Partition> iterator = memory.getPartitions().iterator();

	    int maxCpuTimeToExecute = 0;

	    while (iterator.hasNext()){
	        Process process = processes.get(iterator.next().getPidProcess() - 1);

	        if(process.getCpuTime() > maxCpuTimeToExecute){
	            maxCpuTimeToExecute = process.getCpuTime();
            }

        }

        timeExecution += maxCpuTimeToExecute;

    }

    private void stopCPU(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printFinalRelatorio() {
        System.out.println();
        logExport.add("\n");
        System.out.println();
        logExport.add("\n");
        System.out.println("================== Memoria Finalizada ============== ");
        logExport.add("================== Memoria Finalizada ============== ");
	    System.out.println("Tempo Total de Execução: " + timeExecution + " unidades de CPU ");
        logExport.add("Tempo Total de Execução: " + timeExecution + " unidades de CPU ");
	    System.out.println("Total de fragmentação interna: " + fragmented);
	    logExport.add("Total de fragmentação interna: " + fragmented);
	    System.out.println("Tempo medio de execução: " + (timeExecution / executed) + " unidades de CPU ");
	    logExport.add("Tempo medio de execução: " + (timeExecution / executed) + " unidades de CPU ");
	    System.out.println("Processos executados: " + executed);
	    logExport.add("Processos executados: " + executed);
	    System.out.println("Processos descartados: " + notExecuted);
	    logExport.add("Processos descartados: " + notExecuted);
	    System.out.println("====================================================");
	    logExport.add("====================================================");

    }

    public void exportLog() throws IOException {
        FileOutputStream fos = new FileOutputStream("log.txt");

        for(String log : logExport){
            fos.write(new StringBuilder().append(log).append("\n").toString().getBytes());
        }

        fos.close();

        System.out.print("Arquivo salvo na pasta do projeto");
    }
}
