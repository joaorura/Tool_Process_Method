package change_method;

import code_models.CodeModel;
import com.google.gson.reflect.TypeToken;
import interaction_user.InterfaceProcess;
import utils.Utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static utils.Utils.createJson;
import static utils.Utils.saveInFile;

public class JsonChangeMethodName implements InterfaceProcess {
    private String pathRead, pathWrite;

    public JsonChangeMethodName(String pathRead, String pathWrite) {
        this.pathRead = pathRead;
        this.pathWrite = pathWrite;
    }
    public JsonChangeMethodName(String[] args) throws IndexOutOfBoundsException {
        this(args[0], args[1]);
    }

    private void changeNameMethod(HashMap<String, LinkedList<String>> hashMap, String result, String code) {
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

    private void processJsonAndChangeMethodName() {
        int count = 0, countError = 0;
        HashMap<String, LinkedList<String>> hashMap = new HashMap<>();

        try {
            Type type = new TypeToken<List<CodeModel>>(){}.getType();
            for(CodeModel codeModel : Utils.<CodeModel[]>getJsonData(this.pathRead, type)) {
                try {
                    changeNameMethod(hashMap, codeModel.result, codeModel.code);
                }
                catch (Exception e) {
                    countError += 1;
                }

                count += 1;
            }

            saveInFile(this.pathWrite, createJson(hashMap));
        }
        catch (IOException e) {
            System.out.println("Error in file path or in permissions of SO.");
        }

        System.out.println("Percent of Error: " + ((float) countError/count) + "%");
    }

    @Override
    public void process() {
        this.processJsonAndChangeMethodName();
    }
}
