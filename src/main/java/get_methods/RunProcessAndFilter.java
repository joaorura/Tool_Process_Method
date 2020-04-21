package get_methods;

import code_models.CodeModel;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import utils.CodeUtils;
import utils.StrRunnable;

import java.util.*;

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

    private void setAllNameMethod(String name, String code) {
        CompilationUnit compilationUnit = StaticJavaParser.parse(code);
        try {
            compilationUnit.findAll(MethodDeclaration.class).forEach(c -> c.setName(name));
        } catch (Exception ignore) { return; }

        String theCode = compilationUnit.toString();
        theCode = CodeUtils.removeFirstClass(theCode);
        bufferSaveCode.add(new CodeModel(name, theCode));
    }

    private void processMethods() {
        HashMap<String, String> hashMap;

        try {
            hashMap = new GetAllMethods(this.str).getAnswer();
        }
        catch (Exception e) { return; }

        for(Map.Entry<String, String> pair : hashMap.entrySet()) {
            System.out.print("Processing: "+ animationChars[count] + "\r");
            count = (count + 1) % 4;

            String name = pair.getKey();
            String code = pair.getValue();

            for(String filter : filters) {
                if(name.toLowerCase().contains(filter.toLowerCase())) {
                    setAllNameMethod(name, code);
                    break;
                }
            }
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
