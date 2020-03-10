package process;


import pre_process.FilterAlgorithms;
import pre_process.ProcessFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileProcess {
    private File file;
    private ValidateAlgorithms validateAlgorithms;
    private HashMap<String, ArrayList<String>> algorithms;

    public FileProcess(File file, ValidateAlgorithms validateAlgorithms, HashMap<String, ArrayList<String>> algorithms) {
        this.file = file;
        this.validateAlgorithms = validateAlgorithms;
        this.algorithms = algorithms;
    }

    public void run() {
        ProcessFile theProcessFile = new ProcessFile(this.file);
        String strFile;

        try {
            strFile = theProcessFile.getStringOfFile();
        } catch (IOException e) {
            return;
        }

        FilterAlgorithms filterAlgorithms = new FilterAlgorithms(strFile);
        ArrayList<String> arrayList = filterAlgorithms.getAlgorithms();

        for(String method : arrayList) {
            String test = this.validateAlgorithms.validate(method);

            if(test != null) {
                ArrayList<String> auxArrayList = algorithms.get(test);
                if(auxArrayList == null) {
                    auxArrayList = new ArrayList<>();
                    auxArrayList.add(method);
                    algorithms.put(test, auxArrayList);
                }
                else {
                    auxArrayList.add(method);
                }
            }
        }
    }
}
