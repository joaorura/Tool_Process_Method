package pre_process;

import utils.BufferFile;
import utils.BufferSaveCode;
import utils.ListBuffer;

import java.io.File;

import static utils.Utils.freeMemory;

public class FileLister {
    private String dir;

    private String[] filters;

    private boolean recursive;

    private static final long memoryLimit = 1000;

    public ListBuffer<File> files;

    public FileLister(String dir, String[] filters, ListBuffer<File> files, boolean recursive) {
        this.dir = dir;
        this.filters = filters;
        this.recursive = recursive;
        this.files = files;
    }

    private boolean checkFilters(File file) {
        for (String aux : this.filters) {
            if (!file.toString().endsWith(aux)) {
                return false;
            }
        }

        return true;
    }

    private void processDir(File dir) {
        File [] list = dir.listFiles();
        assert list != null;
        long memory;
        for (File elem : list) {
            memory = freeMemory();
            while (memory <= memoryLimit) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.gc();
                Runtime.getRuntime().gc();
                this.files.process();

                memory = freeMemory();
            }

            if (elem.isDirectory() && this.recursive) {
                processDir(elem);
            } else if(checkFilters(elem)) {
                this.files.add(elem);
            }
        }
    }

    private void listFiles() {
        File d = new File(this.dir);

        if (d.exists() && d.isDirectory()) {
           this.processDir(d);
        }
        else {
            throw new RuntimeException();
        }
    }

    public void processFiles() {
        this.listFiles();
    }

    public static void createFileLister(String pathRead, BufferFile bufferFile, BufferSaveCode bufferSaveCode) {
        FileLister fileLister = new FileLister(pathRead, new String[]{".java"}, bufferFile, true);
        System.out.println("Executing");
        fileLister.processFiles();


        bufferFile.process();
        bufferSaveCode.process();

        try {
            bufferFile.waitForExecution();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
