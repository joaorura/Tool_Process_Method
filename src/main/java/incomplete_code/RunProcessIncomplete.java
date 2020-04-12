package incomplete_code;

import code_models.CodeModel;
import org.javatuples.Pair;
import utils.StrRunnable;

import java.util.LinkedList;
import java.util.List;

import static utils.CodeUtils.getNameOfCode;

public class RunProcessIncomplete extends StrRunnable {
    private static int limit = 1000;
    private static List<CodeModel> bufferSaveCode = new LinkedList<>();

    public RunProcessIncomplete(String str) {
        super(str);
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
        try {
            String name = getNameOfCode(str);
            Pair<String, LinkedList<String>> pair = new RemoveBlocks(name, str, RunProcessIncomplete.limit).call();
            name = pair.getValue0();

            for(String code : pair.getValue1()) {
                RunProcessIncomplete.bufferSaveCode.add(new CodeModel(name, code));
            }
        }
        catch (Exception ignored) { }
    }
}
