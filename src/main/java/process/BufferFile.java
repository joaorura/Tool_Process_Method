package process;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BufferFile extends LinkedList<File> {
    private int size;

    private ThreadPoolExecutor threadPoolExecutor;


    public BufferFile(int size, int amountThreads, String[] filters, BufferSaveCode bufferSaveCode) {
        this.size = size;
        RunProcessAndFilter.setAll(filters, bufferSaveCode);
        this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(amountThreads);
    }


    public void process() {
        for(int i = 0; i <= this.size; i++) {
            File element = super.get(i);
            try {
                String str = new FileProcess(element).process();
                this.threadPoolExecutor.execute(new RunProcessAndFilter(str));
            } catch (RuntimeException e) {
                e.printStackTrace();
                return;
            }
        }

        super.clear();
    }

    public void waitForExecution() throws InterruptedException {
        while(!threadPoolExecutor.isTerminated()) { Thread.sleep(100); }
        this.threadPoolExecutor.shutdown();
    }

    @Override
    public boolean add(File file) {
        if(super.size() == this.size && super.add(file)) {
            process();
            return true;
        }
        else {
            return super.add(file);
        }
    }
}
