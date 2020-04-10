import com.github.javaparser.StaticJavaParser;
import com.google.gson.reflect.TypeToken;
import org.javatuples.Pair;
import plus.*;
import plus.json_models.CodeModel;
import pre_process.FileLister;
import process.FileProcess;
import process.FilterAndGetMethods;
import process.GetAllMethods;
import utils.Utils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;

import static utils.Utils.*;


public class Main {
    private static final ArrayList<String> algorithms = new ArrayList<>();
    private static char[] animationChars = new char[]{'|', '/', '-', '\\'};

    public static void processFiles(String path) {
        FileLister fileLister = new FileLister(path, new String[]{".java"}, true);
        ArrayList<File> files = fileLister.getFiles();
        float percent;
        int count = 0;
        for(int i = 0; i < files.size(); i++) {
            try {
                String str = new FileProcess(files.get(i)).process();
                GetAllMethods getAllMethods = new GetAllMethods(str);
                algorithms.addAll(getAllMethods.getAlgorithms());
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

    public static void changeNameMethod(HashMap<String, LinkedList<String>> hashMap, String result, String code) {
        result = result.replace("['", "").replace("']", "");

        String stringBuilder = "public class " +
                result +
                " {\n" +
                code +
                "\n}";
        ChangeNameMethod changeNameMethod = new ChangeNameMethod(stringBuilder, result);
        LinkedList<String> linkedList = hashMap.get(result);
        if (linkedList == null) {
            linkedList = new LinkedList<>();
            hashMap.put(result, linkedList);
        } else {
            linkedList.add(changeNameMethod.getNewCode());
        }
    }

    public static void processJsonAndChangeMethodName(String[] args, boolean jsonSave) {
        String jsonFile = args[0];
        String pathToSave = args[1];
        int count = 0, countError = 0;
        HashMap<String, LinkedList<String>> hashMap = new HashMap<>();

        try {
            Type type = new TypeToken<List<CodeModel>>(){}.getType();
            for(CodeModel codeModel : Utils.<CodeModel[]>getJsonData(jsonFile, type)) {
                try {
                    changeNameMethod(hashMap, codeModel.result, codeModel.code);
                }
                catch (Exception e) {
                    countError += 1;
                }

                count += 1;
            }

            if(jsonSave) {
                saveInFile(pathToSave, createJson(hashMap));
            }
        }
        catch (IOException e) {
            System.out.println("Error in file path or in permissions of SO.");
        }

        System.out.println("Percent of Error: " + ((float) countError/count) + "%");
    }

    private static void processMethodsAndRemoveLines(String[] args) {
        String pathJson = args[0];
        String pathSave = args[1];
        int numberThreads = 24, size = 0, amount = Integer.parseInt(args[2]);
        LinkedList<Remover> linkedList = new LinkedList<>();
        HashMap<String, LinkedList<String>> hashMap = new HashMap<>();

        Map<String, List<String>> mapInput;
        Type type = new TypeToken<Map<String,  List<String>>>(){}.getType();
        try {
            mapInput = getJsonData(pathJson, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }



        for(Map.Entry<String, List<String>> entry : mapInput.entrySet()) {
            size += entry.getValue().size();
            for(String code : entry.getValue()) {
                RemoveBlocks remove = new RemoveBlocks(entry.getKey(), code, amount);
                linkedList.add(remove);
            }
        }

        RemoveBlocks.setAllAmount(size);
        RemoveBlocks.resetAmount();

        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberThreads);
        List<Future<Pair<String, LinkedList<String>>>> list;
        try {
            list = executorService.invokeAll(linkedList);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }


        for(Future<Pair<String, LinkedList<String>>> future : list) {
            Pair<String, LinkedList<String>> pair;
            try { pair = future.get(); }
            catch (InterruptedException | ExecutionException | CancellationException e) {
                continue;
            }

            LinkedList<String> auxLinkedList = hashMap.get(pair.getValue0());
            if(auxLinkedList == null) { hashMap.put(pair.getValue0(), pair.getValue1()); }
            else {
                for (String aux : pair.getValue1()) {
                    if (!auxLinkedList.contains(aux)) { auxLinkedList.add(aux); }
                }
            }
        }

        executorService.shutdown();
        System.out.println("Process end!!");
        System.out.println("Json Saved ");
        saveInFile(pathSave, createJson(hashMap));

    }

    private static Map<String, List<String>> getDataJsonMap(String newArg) {
        Type type = new TypeToken<Map<String,  List<String>>>(){}.getType();
        try {
            return getJsonData(newArg, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static void processFilterAndGetMethods(String[] args) {
        String[] filters = new String[] { "search", "filter", "sort" };
        FilterAndGetMethods filterAndGetMethods = new FilterAndGetMethods(args[0], filters, args[1], Integer.parseInt(args[2]));
        filterAndGetMethods.process();

        saveInFile(args[3], createJson(filterAndGetMethods.examples));
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
                processJsonAndChangeMethodName(newArgs, true);
                break;
            case 2:
                Map<String, List<String>> map = getDataJsonMap(newArgs[0]);
                new CreateRepositorie(map, newArgs[1]).process();
                break;
            case 3:
                processMethodsAndRemoveLines(newArgs);
                break;
            case 4:
                processFilterAndGetMethods(newArgs);

            default:
                System.out.println("Error in type described, must be 0 in 1");
        }

        System.out.println("End of Exectuion");
    }



}
