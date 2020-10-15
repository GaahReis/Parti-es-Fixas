package simuladormemoriafixas.Process;

public class Process {
    
    private int pid;
    private int size;
    private int cpuTime;
    private boolean upruning;
    
    public Process(int pid, int size, int cpuTime) {
        this.pid = pid;
        this.size = size;
        this.cpuTime = cpuTime;
    }

    public int getSize() {
        return size;
    }

    public int getPid() {
        return pid;
    }

    public synchronized boolean isUpruning() {
        return upruning;
    }

    public synchronized void setUpruning(boolean upruning) {
        this.upruning = upruning;
    }

    public synchronized int getCpuTime() {
        return cpuTime;
    }

    public synchronized void setCpuTime(int cpuTime) {
        this.cpuTime = cpuTime;
    }
}
