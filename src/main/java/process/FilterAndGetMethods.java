package process;

import pre_process.FileLister;

import java.util.ArrayList;


public class FilterAndGetMethods {
    private String pathFile;

    public final ArrayList<String> examples = new ArrayList<>();

    private BufferSaveCode bufferSaveCode;

    private BufferFile bufferFile;
    public FilterAndGetMethods(String pathFile, String[] filters, String path, int size, int amountThreads) {
        this.pathFile = pathFile;

        this.bufferSaveCode = new BufferSaveCode(size, path, examples);
        this.bufferFile = new BufferFile(size, amountThreads, filters, this.bufferSaveCode);
    }



    public void process() {
        FileLister fileLister = new FileLister(this.pathFile, new String[]{".java"}, this.bufferFile, true);
        fileLister.processFiles();
        bufferSaveCode.process();
        bufferFile.process();

        try {
            bufferFile.waitForExecution();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
