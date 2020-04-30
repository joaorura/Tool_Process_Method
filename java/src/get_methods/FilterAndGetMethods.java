package get_methods;

import pre_process.FileLister;
import utils.BufferFile;
import utils.BufferSaveCode;

import java.util.ArrayList;


public class FilterAndGetMethods {
    private String pathFile;

    public final ArrayList<String> examples = new ArrayList<>();


    private BufferFile bufferFile;

    public FilterAndGetMethods(String pathFile, String[] filters, String path, int size, int amountThreads) {
        this.pathFile = pathFile;

        BufferSaveCode bufferSaveCode = new BufferSaveCode(size, path, examples);
        RunProcessAndFilter.setAll(filters, bufferSaveCode);

        this.bufferFile = new BufferFile(size, RunProcessAndFilter.class, amountThreads, bufferSaveCode);
    }



    public void process() {
        FileLister fileLister = new FileLister(this.pathFile, new String[]{".java"}, this.bufferFile, true);
        System.out.println("Executing");
        fileLister.processFiles();

        try {
            bufferFile.waitForExecution();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
