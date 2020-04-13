package repositories;

import code_models.CodeModel;
import utils.StrRunnable;

import java.util.LinkedList;
import java.util.List;

import static utils.CodeUtils.getNameOfClass;

public class RunDivideInRepositories extends StrRunnable {
    private static List<CodeModel> bufferSaveCode = new LinkedList<>();

    public RunDivideInRepositories(String str) {
        super(str);
    }

    public static void setAll(List<CodeModel> bufferSaveCode) {
        RunDivideInRepositories.bufferSaveCode = bufferSaveCode;
    }

    @Override
    public void run() {
        String name = getNameOfClass(str);
        RunDivideInRepositories.bufferSaveCode.add(new CodeModel(name, str));
    }
}
