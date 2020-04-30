package repositories;

import code_models.CodeModel;
import utils.CodeUtils;
import utils.StrRunnable;

import java.util.List;

import static utils.CodeUtils.getNameOfClassFromFile;
import static utils.Utils.animationChars;

public class RunDivideInRepositories extends StrRunnable {
    private static List<CodeModel> bufferSaveCode;
    private String nameFile;

    private static  int count = 0;

    public RunDivideInRepositories(String str, String path) {
        super(str);

        this.nameFile = getNameOfClassFromFile(path);
    }

    public static void setAll(List<CodeModel> saveCode) {
        bufferSaveCode = saveCode;
    }

    @Override
    public void run() {
        System.out.print("Processing: " + animationChars[count] + "\r");
        count = (count + 1) % 4;
        this.str = CodeUtils.removeFirstClass(str );
        RunDivideInRepositories.bufferSaveCode.add(new CodeModel(nameFile, str));
    }
}
