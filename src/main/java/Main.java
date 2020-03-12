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
    private static char[] animationChars = new char[]{'|', '/', '-', '\\'};


    public static String createJson() {
        Gson gson = new Gson();
        return gson.toJson(algorithms);
    }

    public static void saveJson(String thePath, String json)  {
        Path path = Paths.get(thePath);

        try {
            Files.createFile(path);
        } catch (Exception e) {
            File file = new File(path.toString());
            boolean bool = file.delete();

            if(bool) {
                try {
                    Files.createFile(path);
                }
                catch (IOException a) {
                    throw new RuntimeException("Não está sendo possível criar o arquivo.");
                }
            }
            else {
                throw new RuntimeException("O delete do arquivo falhou e o mesmo é existente.");
            }
        }

        try {
            Files.write(path, json.getBytes(), StandardOpenOption.WRITE);
        }
        catch (IOException e) {
            throw new RuntimeException("Não está sendo possível escrever no arquivo criado.");
        }
    }

    public static void processFiles(String path) {
        FileLister fileLister = new FileLister(path, new String[]{".java"}, true);
        ArrayList<File> files = fileLister.getFiles();
        float percent;
        int count = 0;
        for(int i = 0; i < files.size(); i++) {
            try {
                new FileProcess(files.get(i), algorithms).process();
            }
            catch (RuntimeException e) {
                System.out.println(e.getMessage());
                count++;
            }
            percent = ((float) i /  files.size()) * 100;
            System.out.print("Processing: " + percent + "% " + animationChars[i % 4] + "\r");
        }

        System.out.println("Process Done!!");
        System.out.println("Porcentagem de arquivos com erros:" + (((float) count / files.size()) * 100) + "%");
    }

    public static void main(String[] args) {
        if(args.length != 2) {
            throw new RuntimeException("Argumentos do Programa em Falta");
        }

        System.out.println("Initial path: " + args[0]);
        System.out.println("Data path: " +  args[1]);

        processFiles(args[0]);

        try {
            String json = createJson();
            saveJson(args[1], json);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n\nError na cricao da String json ou no salvamente desta");
        }

    }
}
