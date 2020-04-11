package process;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import plus.json_models.CodeModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static utils.Utils.animationChars;

public class RunProcessAndFilter implements Runnable{
    private static List<CodeModel> bufferSaveCode = new LinkedList<>();

    private static int count = 0;

    private static String[] filters = {};

    private String str;

    public RunProcessAndFilter(String str) {
        this.str = str;
    }

    public static void setAll(String[] filters, List<CodeModel> bufferSaveCode) {
        RunProcessAndFilter.filters = filters;
        RunProcessAndFilter.bufferSaveCode = bufferSaveCode;
    }

    private void setAllNameMethod(String name, ArrayList<String> arrayList) {
        for(int i = 0; i <= arrayList.size(); i++) {
            String theCode = "public class Test {\n" + arrayList.get(i) + "\n}\n";
            CompilationUnit compilationUnit = StaticJavaParser.parse(theCode);
            compilationUnit.findFirst(MethodDeclaration.class).get().setName(name);
            theCode = compilationUnit.toString().substring(21, arrayList.size() - 3);
            bufferSaveCode.add(new CodeModel(name, theCode));
        }
    }

    private void processMethods() {
        GetAllMethods getAllMethods = new GetAllMethods(this.str);
        ArrayList<String> arrayList = getAllMethods.getAlgorithms();

        for(String code : arrayList) {
            String theCode = "public class Test {\n" + code + "\n}\n";
            CompilationUnit compilationUnit = StaticJavaParser.parse(theCode);
            String name = compilationUnit.findFirst(MethodDeclaration.class).get().getNameAsString();

            for(String filter : filters) {
                if(name.contains(filter)) {
                    setAllNameMethod(name, arrayList);
                    return;
                }
            }
        }

        System.out.print("Processing: "+ animationChars[count] + "\r");
        count += 1;
        count %= 4;
    }

    @Override
    public void run() {
        processMethods();
    }
}
