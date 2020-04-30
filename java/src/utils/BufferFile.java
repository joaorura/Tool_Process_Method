package utils;

import code_models.CodeModel;
import get_methods.RunProcessAndFilter;
import get_methods.RunSetGetMethods;
import incomplete_code.RunProcessIncomplete;
import pre_process.FileProcess;
import repositories.RunDivideInRepositories;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static utils.Utils.usedMemory;

public class BufferFile extends ListBuffer<File> {
    private Class runnableClass;

    private ListBuffer<CodeModel> listBuffer;

    private ThreadPoolExecutor threadPoolExecutor;

    private static int memoryLimit = 4000;

    public BufferFile(int size, Class runnableClass, int numberOfThread, ListBuffer<CodeModel> listBuffer) throws RuntimeException{
        super(size);

        if(! (runnableClass.equals(RunProcessAndFilter.class) ||
                runnableClass.equals(RunProcessIncomplete.class) ||
                runnableClass.equals(RunDivideInRepositories.class) ||
                runnableClass.equals(RunSetGetMethods.class))) {
            throw new RuntimeException("The action of RunnableClass its not implemented.");
        }

        this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfThread);
        this.listBuffer = listBuffer;
        this.runnableClass = runnableClass;
    }

    protected String processFile(File file) {
        return new FileProcess(file).process();
    }

    public boolean process(boolean mem) {
        super.lock = true;

        File element;

        for(int i = 0; i < super.size(); i++) {
            if(mem && usedMemory() >= memoryLimit) {
                super.lock = false;
                return false;
            }

            try {
                element = super.get(0);
                super.remove(0);
                String str = processFile(element);

                Runnable runnable = null;

                if(runnableClass.equals(RunProcessAndFilter.class)) {
                    runnable = new RunProcessAndFilter(str);
                }
                else if(runnableClass.equals(RunProcessIncomplete.class)) {
                    runnable = new RunProcessIncomplete(str, element.toString());
                }
                else if(runnableClass.equals(RunDivideInRepositories.class)) {
                    runnable = new RunDivideInRepositories(str, element.toString());
                }
                else if(runnableClass.equals(RunSetGetMethods.class)) {
                    runnable = new RunSetGetMethods(str, element.toString());
                }

                if(str != null && runnable != null) {
//                    runnable.run();
                    this.threadPoolExecutor.execute(runnable);
                }
            }
            catch (Exception ignore) { }
        }

        this.listBuffer.process(true);
        super.lock = false;

        return true;
    }

    @Override
    public boolean add(File file) {
        super.lock = true;

        long memory = usedMemory();
        while (memory >= memoryLimit) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.gc();
            Runtime.getRuntime().gc();

            memory = usedMemory();
        }

        super.lock = false;

        return super.add(file);
    }

    public void waitForExecution() throws InterruptedException {
        while(super.size() != 0) {
            Thread.sleep(1000);
            this.process(true);
        }

        System.out.println("Wating a Pool shutdow: " + threadPoolExecutor.getQueue().size());
        while(threadPoolExecutor.getQueue().size() != 0) {
            Thread.sleep(1000);
        }

        this.threadPoolExecutor.shutdown();

        System.out.println("Wating a Pool terminarted");
        while(!threadPoolExecutor.isTerminated()) {
            Thread.sleep(1000);
        }

        while(listBuffer.size() != 0) {
            Thread.sleep(1000);
            listBuffer.process(true);
        }
    }
}
