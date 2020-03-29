import com.github.javaparser.StaticJavaParser;
import com.google.gson.Gson;
import org.javatuples.Pair;
import plus.*;
import plus.json_models.CodeModel;
import pre_process.FileLister;
import process.FileProcess;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

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

    private static CodeModel[] getJsonData(String jsonFile) throws FileNotFoundException {
        FileReader fileReader = new FileReader(jsonFile);
        return new Gson().fromJson(fileReader, CodeModel[].class);
    }

    public static HashMap<String, ArrayList<String>> processCsvAndChangeMethodName(String[] args, boolean jsonSave) {
        String jsonFile = args[0];
        String pathToSave = args[1];
        int count = 0, countError = 0;
        HashMap<String, ArrayList<String>> hashMap = new HashMap<>();

        try {
            for(CodeModel codeModel : getJsonData(jsonFile)) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("public class ");
                    codeModel.result = codeModel.result.replace("['", "").replace("']", "");
                    stringBuilder.append(codeModel.result);
                    stringBuilder.append(count);
                    stringBuilder.append("Class");
                    stringBuilder.append(" {\n");
                    stringBuilder.append(codeModel.code);
                    stringBuilder.append("\n}");

                    ChangeNameMethod changeNameMethod = new ChangeNameMethod(stringBuilder.toString(), codeModel.result);
                    ArrayList<String> arrayList = hashMap.get(codeModel.result);
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

    private static void processMethodsAndRemoveLines(String[] args) {
        String pathJson = args[0];
        String pathSave = args[1];
        int numberThreads = 12 * 300;
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberThreads);
        LinkedList<Remover> linkedList = new LinkedList<>();
        HashMap<String, LinkedList<String>> hashMap = new HashMap<>();

        try {
            CodeModel[] codeModels = getJsonData(pathJson);
            Remover.setAllAmount(codeModels.length);
            for(CodeModel codeModel : codeModels) {
                RemoveBlocks remove = new RemoveBlocks(codeModel.code, codeModel.result);
                linkedList.add(remove);
            }

            List<Future<Pair<String, LinkedList<String>>>> list = threadPoolExecutor.invokeAll(linkedList);
            while (!threadPoolExecutor.isTerminated()) {
                System.out.println("Waiting\n");
            }

            for(Future<Pair<String, LinkedList<String>>> future : list) {
                Pair<String, LinkedList<String>> pair = future.get();
                LinkedList<String> auxLinkedList = hashMap.get(pair.getValue0());
                if(auxLinkedList == null) {
                    hashMap.put(pair.getValue0(), pair.getValue1());
                }
                else { auxLinkedList.addAll(pair.getValue1()); }
            }

            System.out.println("Save json");
            saveInFile(pathSave, createJson(hashMap));
        } catch (FileNotFoundException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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
            case 3:
                processMethodsAndRemoveLines(newArgs);
                break;
            default:
                System.out.println("Error in type described, must be 0 in 1");
        }

    }
}
