package process;


import pre_process.ProcessFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
                byte[] utf8 = strFile.getBytes(StandardCharsets.UTF_8);
                strFile = new String(utf8, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Error ao ler arquivo e converter para UTF-8");
            }

            try {
                FilterCode filterCode = new FilterCode(strFile);
                this.algorithms.addAll(filterCode.getAlgorithms());
            } catch (Exception e) {
                throw  new RuntimeException("Error processing file: " + this.file + "\n");
            }
        }
    }
}
