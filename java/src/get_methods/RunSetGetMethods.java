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



    public RunSetGetMethods(String str, String fileName) {
        super(str);

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
            String name = CodeUtils.getNameOfClass(str).replaceAll("_*[0-9]*", "");
            if(!examples.contains(name)) { examples.add(name); }

            CodeModel codeModel = CodeUtils.setAllNameMethod(name, str);
            bufferSaveCode.add(codeModel);
        }
        catch (RuntimeException e) {
            System.out.println("Error");
        }

    }
}
