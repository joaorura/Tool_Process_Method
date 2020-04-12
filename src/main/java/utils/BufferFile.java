package utils;

import code_models.CodeModel;
import get_methods.RunProcessAndFilter;
import incomplete_code.RunProcessIncomplete;
import pre_process.FileProcess;
import repositories.RunDivideInRepositories;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BufferFile extends ListBuffer<File> {
    private Class<StrRunnable> runnableClass;
    private ListBuffer<CodeModel> listBuffer;
    private ThreadPoolExecutor threadPoolExecutor;

    public BufferFile(int size, Class runnableClass, int numberOfThread, ListBuffer<CodeModel> listBuffer) throws RuntimeException{
        super(size);

        if(! (runnableClass.equals(RunProcessAndFilter.class) ||
                runnableClass.equals(RunProcessIncomplete.class) ||
                runnableClass.equals(RunDivideInRepositories.class))) {
            throw new RuntimeException("The action of RunnableClass its not implemented.");
        }

        this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfThread);
        this.listBuffer = listBuffer;
        this.runnableClass = runnableClass;
    }

    public boolean process() {
        super.lock = true;
        File element;

        for(int i = 0; i < super.size; i++) {
            try {
                element = super.get(0);
                super.remove(0);
                String str = new FileProcess(element).process();

                Runnable runnable = null;

                if(runnableClass.equals(RunProcessAndFilter.class)) {
                    runnable = new RunProcessAndFilter(str);
                }
                else if(runnableClass.equals(RunProcessIncomplete.class)) {
                    runnable = new RunProcessIncomplete(str);
                }
                else if(runnableClass.equals(RunDivideInRepositories.class)) {
                    runnable = new RunDivideInRepositories(str);
                }

                if(str != null && runnable != null) {
                    this.threadPoolExecutor.execute(runnable);
                }
            }
            catch (Exception ignore) {
                this.lock = false;
                return false;
            }
        }

        this.listBuffer.process();
        super.lock = false;

        return true;
    }

    public void waitForExecution() throws InterruptedException {
        this.threadPoolExecutor.shutdown();
        while(!threadPoolExecutor.isTerminated()) {
            System.out.println("Waiting end off pool");;
            Thread.sleep(100);
        }
    }
}
