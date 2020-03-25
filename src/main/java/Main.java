import com.github.javaparser.StaticJavaParser;
import com.google.gson.Gson;
import plus.ChangeNameMethod;
import plus.CodeModel;
import plus.CreateRepositorie;
import pre_process.FileLister;
import process.FileProcess;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static utils.Utils.saveInFile;


public class Main {
    private static final ArrayList<String> algorithms = new ArrayList<>();
    private static char[] animationChars = new char[]{'|', '/', '-', '\\'};


    public static String createJson(Object element) {
        Gson gson = new Gson();
        return gson.toJson(element);
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
        System.out.println("Numéro de métodos colétados: " + algorithms.size());
    }

    public static void processFileAndGetMethods(String[] args) {
        if(args.length != 2) {
            throw new RuntimeException("Argumentos do Programa em Falta");
        }

        System.out.println("Initial path: " + args[0]);
        System.out.println("Data path: " +  args[1]);

        processFiles(args[0]);

        try {
            String json = createJson(algorithms);
            saveInFile(args[1], json);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n\nError na cricao da String json ou no salvamente desta");
        }
    }

    public static HashMap<String, ArrayList<String>> processCsvAndChangeMethodName(String[] args, boolean jsonSave) {
        String csvFile = args[0];
        String pathToSave = args[1];
        StringBuilder stringBuilder;
        CodeModel[] codeModels;
        FileReader fileReader;
        ChangeNameMethod changeNameMethod;
        ArrayList<String> arrayList;
        HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
        int count = 0, countError = 0;

        try {
            fileReader = new FileReader(csvFile);
            codeModels =  new Gson().fromJson(fileReader, CodeModel[].class);

            for(CodeModel codeModel : codeModels) {
                try {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("public class ");
                    codeModel.result = codeModel.result.replace("['", "").replace("']", "");
                    stringBuilder.append(codeModel.result);
                    stringBuilder.append(count);
                    stringBuilder.append("Class");
                    stringBuilder.append(" {\n");
                    stringBuilder.append(codeModel.code);
                    stringBuilder.append("\n}");

                    changeNameMethod = new ChangeNameMethod(stringBuilder.toString(), codeModel.result);
                    arrayList = hashMap.get(codeModel.result);
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                        hashMap.put(codeModel.result, arrayList);
                    } else {
                        arrayList.add(changeNameMethod.getNewCode());
                    }
                }
                catch (Exception e) {
                    countError += 1;
                }

                count += 1;
            }

            if(jsonSave) {
                saveInFile(pathToSave, new Gson().toJson(hashMap));
            }
        }
        catch (IOException e) {
            System.out.println("Error in file path or in permissions of SO.");
        }

        System.out.println("Percent of Error: " + ((float) countError/count) + "%");
        return hashMap;
    }

    public static void main(String[] args) {
        StaticJavaParser.getConfiguration().setAttributeComments(false);
        int type = Integer.parseInt(args[0]);
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

        switch (type) {
            case 0:
                processFileAndGetMethods(newArgs);
                break;
            case 1:
                processCsvAndChangeMethodName(newArgs, true);
                break;
            case 2:
                HashMap<String, ArrayList<String>> hashMap = processCsvAndChangeMethodName(newArgs, false);
                new CreateRepositorie(hashMap, newArgs[1]).process();
                break;
            default:
                System.out.println("Error in type described, must be 0 in 1");
        }

    }
}
