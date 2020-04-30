package incomplete_code;

import code_models.CodeModel;
import org.javatuples.Pair;
import utils.StrRunnable;
import utils.Utils;

import java.util.LinkedList;
import java.util.List;

import static utils.CodeUtils.*;

public class RunProcessIncomplete extends StrRunnable {
    private static int limit = 1000, time = 0;

    private static List<CodeModel> bufferSaveCode = null;

    private String file;

    public RunProcessIncomplete(String str, String path) {
        super(str);

        this.file = getNameOfClassFromFile(path);
    }

    public static void setAll(List<CodeModel> bufferSaveCode) {
        RunProcessIncomplete.bufferSaveCode = bufferSaveCode;
    }

    public static void setAll(List<CodeModel> bufferSaveCode, int limit) {
        RunProcessIncomplete.setAll(bufferSaveCode);
        RunProcessIncomplete.limit = limit;
    }


    @Override
    public void run() {
        System.out.print("Processing: " + Utils.animationChars[time % 4] + "\r");

        time ++;

        try {
            String name = file;

            Pair<String, LinkedList<String>> pair = new RemoveBlocks(name, str, RunProcessIncomplete.limit).call();

            name = pair.getValue0();

            for(String code : pair.getValue1()) {
                String newCode = removeFirstClass(code);

                bufferSaveCode.add(new CodeModel(name, newCode));
            }
        }
        catch (Exception e) {
            System.out.println("Problema ao processar codigo para gerar incompleto.");
        }
    }
}
