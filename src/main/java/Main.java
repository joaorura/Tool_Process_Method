import com.google.gson.Gson;
import pre_process.FileLister;
import process.FileProcess;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;


public class Main {
    private static final ArrayList<String> algorithms = new ArrayList<>();

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

    public static void processFiles(String path) {
        FileLister fileLister = new FileLister(path, new String[]{".java"}, true);
        ArrayList<File> files = fileLister.getFiles();

        for(File aux : files) {
            new FileProcess(aux, algorithms).process();
        }
    }

    public static void main(String[] args) {
        if(args.length != 2) {
            throw new RuntimeException("Argumentos do Programa em Falta");
        }

        System.out.println("Initial path: " + args[0]);
        System.out.println("Data path: " +  args[1]);

        try {
            processFiles(args[0]);
            String json = createJson();
            saveJson(args[1], json);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }
}
