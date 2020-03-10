import com.google.gson.Gson;
import pre_process.FileLister;
import pre_process.FilterAlgorithms;
import pre_process.ProcessFile;
import process.TestValidate;
import process.ValidateAlgorithms;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private static final HashMap<String, ArrayList<String>> algorithms = new HashMap<>();

    public static String createJson() {
        Gson gson = new Gson();
        return gson.toJson(algorithms);
    }

    public static void saveJson(String thePath, String json) throws IOException {
        Path path = Paths.get(thePath);

        try {
            Files.createFile(path);
        } catch (Exception e) {
            File file = new File(path.toString());
            boolean bool = file.delete();
            if(bool) {
                Files.createFile(path);
            }
            else {
                throw new RuntimeException();
            }
        }

        Files.write(path, json.getBytes(), StandardOpenOption.WRITE);
    }

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

        String json = createJson();
        saveJson(args[1], json);
    }
}
