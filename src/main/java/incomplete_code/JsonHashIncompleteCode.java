package incomplete_code;

import com.google.gson.reflect.TypeToken;
import interaction_user.InterfaceProcess;
import org.javatuples.Pair;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static java.lang.Integer.parseInt;
import static utils.Utils.*;

public class JsonHashIncompleteCode implements InterfaceProcess {
    private int limitOfCodes, numberThreads;

    private String pathRead, pathWrite;

    public JsonHashIncompleteCode(int limitOfCodes, int numberThreads, String pathRead, String pathWrite) {
        this.limitOfCodes = limitOfCodes;
        this.numberThreads = numberThreads;
        this.pathRead = pathRead;
        this.pathWrite = pathWrite;
    }


    public JsonHashIncompleteCode(String[] args) throws IndexOutOfBoundsException {
        this.limitOfCodes = parseInt(args[0]);
        this.numberThreads = parseInt(args[1]);
        this.pathRead = args[2];
        this.pathWrite = args[3];
    }

    private Map<String, List<String>> getData() throws FileNotFoundException {
        Map<String, List<String>> mapInput;
        Type type = new TypeToken<Map<String,  List<String>>>(){}.getType();
        mapInput = getJsonData(this.pathRead, type);

        return mapInput;
    }

    private List<RemoveBlocks> createListProcess() throws FileNotFoundException {
        int size = 0;

        LinkedList<RemoveBlocks> linkedList = new LinkedList<>();

        Map<String, List<String>> map = getData();


        for(Map.Entry<String, List<String>> entry : map.entrySet()) {
            size += entry.getValue().size();

            for(String code : entry.getValue()) {
                RemoveBlocks remove = new RemoveBlocks(entry.getKey(), code, this.limitOfCodes);
                linkedList.add(remove);
            }
        }

        RemoveBlocks.setAllAmount(size);
        return linkedList;
    }

    private HashMap<String, LinkedList<String>> getProcessedData(List<Future<Pair<String, LinkedList<String>>>> futureList) {
        HashMap<String, LinkedList<String>> hashMap = new HashMap<>();

        for(Future<Pair<String, LinkedList<String>>> future : futureList) {
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

        return hashMap;
    }

    private void createInvokeProcess() throws FileNotFoundException, InterruptedException {
        List<RemoveBlocks> listProcess = createListProcess();
        RemoveBlocks.resetAmount();

        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.numberThreads);
        List<Future<Pair<String, LinkedList<String>>>> list;
        list = executorService.invokeAll(listProcess, 5, TimeUnit.HOURS);

        executorService.shutdownNow();

        while(!executorService.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) { }
        }

        System.out.println("Process end!!");

        HashMap<String, LinkedList<String>> hashMap = getProcessedData(list);

        System.out.println("Json Saved ");

        saveInFile(this.pathWrite, createJson(hashMap));
    }

    @Override
    public void process() {
        try {
            createInvokeProcess();
        } catch (FileNotFoundException | InterruptedException e) {
            throw new RuntimeException("Error of Execution", e);
        }
    }
}
