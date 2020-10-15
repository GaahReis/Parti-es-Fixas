package simuladormemoriafixas.Memory;

public class Partition {

    private int size;
    private int pidProcess;
    private int processSizeInPartition;

    Partition(int size){
        this.size = size;
    }

    public int getSize() {
        return size;
    }


    public int getPidProcess() {
        return pidProcess;
    }

    public void setPidProcess(int pidProcess) {
        this.pidProcess = pidProcess;
    }

    public int getProcessSizeInPartition() {
        return processSizeInPartition;
    }

    public void setProcessSizeInPartition(int processSizeInPartition) {
        this.processSizeInPartition = processSizeInPartition;
    }
}
