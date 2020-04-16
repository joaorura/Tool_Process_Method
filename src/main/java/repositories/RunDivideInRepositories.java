package repositories;

import code_models.CodeModel;
import utils.StrRunnable;

import java.util.LinkedList;
import java.util.List;

import static utils.Utils.animationChars;

public class RunDivideInRepositories extends StrRunnable {
    private static List<CodeModel> bufferSaveCode = new LinkedList<>();

    private String nameFile;

    private int count = 0;

    public RunDivideInRepositories(String str, String path) {
        super(str);
        String[] splited  = path.split("/");
        this.nameFile = splited[splited.length - 1];
    }

    public static void setAll(List<CodeModel> bufferSaveCode) {
        RunDivideInRepositories.bufferSaveCode = bufferSaveCode;
    }

    @Override
    public void run() {
        System.out.print("Processing: " + animationChars[count] + "\r");
        count = (count + 1) % 4;

        RunDivideInRepositories.bufferSaveCode.add(new CodeModel(nameFile, str));
    }
}
