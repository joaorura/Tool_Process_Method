package get_methods;

import code_models.CodeModel;
import utils.BufferSaveCode;
import utils.CodeUtils;
import utils.StrRunnable;

import java.util.List;

import static utils.Utils.animationChars;

public class RunSetGetMethods extends StrRunnable {
    private static BufferSaveCode bufferSaveCode;

    private static List<String> examples;

    private static  int count = 0;

    private String fileName, nameClass;


    public RunSetGetMethods(String str, String fileName) {
        super(str);

        this.fileName = fileName;
        this.nameClass = CodeUtils.getNameOfClassFromFile(fileName);
        if(!CodeUtils.containAClass(str)) {
            super.str = "public class " + CodeUtils.getNameOfClassFromFile(fileName) + " {\n" + str + "\n}\n";
        }
    }

    public static void setAll(BufferSaveCode bufferSaveCode, List<String> examples) {
        RunSetGetMethods.bufferSaveCode = bufferSaveCode;
        RunSetGetMethods.examples = examples;
    }

    @Override
    public void run() {
        System.out.print("Processing: " + animationChars[count] + "\r");
        count = (count + 1) % 4;

        try {
            if(!examples.contains(this.nameClass)) { examples.add(this.nameClass); }

            CodeModel codeModel = CodeUtils.setAllNameMethod(this.nameClass, str);
            bufferSaveCode.add(codeModel);
        }
        catch (RuntimeException e) {
            System.out.println("Error!!\tCodePath:" + this.fileName);
            e.printStackTrace();
        }

    }
}
