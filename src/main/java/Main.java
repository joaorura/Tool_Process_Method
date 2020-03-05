import pre_process.FileLister;
import pre_process.FilterAlgorithms;
import pre_process.ProcessFile;
import process.TestValidate;
import process.ValidateAlgorithms;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final HashMap<String, ArrayList<String>> algorithms = new HashMap<>();


    public static void main(String[] args) throws IOException {
        System.out.println("Initial path: " + args[0]);
        FileLister fileLister = new FileLister(args[0], new String[]{".java"}, true);
        ArrayList<File> files = fileLister.getFiles();
        ValidateAlgorithms validateAlgorithms = new TestValidate();

        for(File aux : files) {

            ProcessFile processFile = new ProcessFile(aux);
            String strFile = processFile.getStringOfFile();
            FilterAlgorithms filterAlgorithms = new FilterAlgorithms(strFile);
            ArrayList<String> arrayList = filterAlgorithms.getAlgorithms();

            for(String method : arrayList) {
                String test = validateAlgorithms.validate(method);

                //Salvar Resultado Por Interecao
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

        //Salvar Resultado _Todo
        System.out.println("Result");
        System.out.println(algorithms);
    }
}
