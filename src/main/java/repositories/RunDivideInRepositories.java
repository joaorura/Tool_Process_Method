package repositories;

import code_models.CodeModel;
import utils.StrRunnable;
import utils.Utils;

import java.util.LinkedList;
import java.util.List;

import static utils.CodeUtils.getNameOfClass;
import static utils.Utils.animationChars;

public class RunDivideInRepositories extends StrRunnable {
    private static List<CodeModel> bufferSaveCode = new LinkedList<>();

    private int count = 0;

    public RunDivideInRepositories(String str) {
        super(str);
    }

    public static void setAll(List<CodeModel> bufferSaveCode) {
        RunDivideInRepositories.bufferSaveCode = bufferSaveCode;
    }

    @Override
    public void run() {
        String name;

        System.out.print("Processing: " + animationChars[count] + "\r");
        count = (count + 1) % 4;

        try {
            name = getNameOfClass(str);
        }
        catch (RuntimeException e) { return; }

        RunDivideInRepositories.bufferSaveCode.add(new CodeModel(name, str));
    }
}
