package get_methods;

import code_models.CodeModel;
import utils.StrRunnable;

import java.util.*;

import static utils.CodeUtils.setAllNameMethod;
import static utils.Utils.animationChars;

public class RunProcessAndFilter extends StrRunnable {
    private static List<CodeModel> bufferSaveCode = new LinkedList<>();

    private static int count = 0;
    private static String[] filters = {};

    public RunProcessAndFilter(String str) {
        super(str);
    }

    public static void setAll(String[] filters, List<CodeModel> bufferSaveCode) {
        RunProcessAndFilter.filters = filters;
        RunProcessAndFilter.bufferSaveCode = bufferSaveCode;
    }


    private void processMethods() {
        HashMap<String, String> hashMap;
        CodeModel codeModel;

        try {
            hashMap = new GetAllMethods(this.str).getAnswer();
        }
        catch (Exception e) { return; }

        for(Map.Entry<String, String> pair : hashMap.entrySet()) {
            System.out.print("Processing: "+ animationChars[count] + "\r");
            count = (count + 1) % 4;

            String name = pair.getKey();
            String code = pair.getValue();

            try {
                if(filters == null) {
                    codeModel = setAllNameMethod(name, code);
                    bufferSaveCode.add(codeModel);
                }
                else {
                    for(String filter : filters) {
                        if(name.toLowerCase().contains(filter.toLowerCase())) {
                            codeModel = setAllNameMethod(name, code);
                            bufferSaveCode.add(codeModel);
                            break;
                        }
                    }
                }
            }
            catch (RuntimeException ignore) { }

        }
    }

    @Override
    public void run() {
        try {
            processMethods();
        }
        catch (Exception ignore) { }
    }
}
