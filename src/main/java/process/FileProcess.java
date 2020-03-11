package process;


import pre_process.ProcessFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileProcess {
    private File file;
    private ArrayList<String> algorithms;
    private boolean process = false;
    public FileProcess(File file, ArrayList<String> algorithms) {
        this.file = file;
        this.algorithms = algorithms;
    }

    public void process() {
        if(!this.process) {
            this.process = true;

            ProcessFile theProcessFile = new ProcessFile(this.file);
            String strFile;
            try {
                strFile = theProcessFile.getStringOfFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            try {
                FilterCode filterCode = new FilterCode(strFile);
                this.algorithms.addAll(filterCode.getAlgorithms());
            } catch (Exception e) {
                System.out.println("Error processing file: " + this.file);
            }
        }
    }
}
