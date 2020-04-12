package get_methods;

import code_models.CodeModel;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import utils.StrRunnable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static utils.Utils.animationChars;

public class RunProcessAndFilter extends StrRunnable {
    private static List<CodeModel> bufferSaveCode = new LinkedList<>();

    private static int count = 0, limit = 1000;
    private static String[] filters = {};

    public RunProcessAndFilter(String str) {
        super(str);
    }

    public static void setAll(String[] filters, List<CodeModel> bufferSaveCode, int limit) {
        RunProcessAndFilter.filters = filters;
        RunProcessAndFilter.bufferSaveCode = bufferSaveCode;
        RunProcessAndFilter.limit = limit;
    }

    public static void setAll(String[] filters, List<CodeModel> bufferSaveCode) {
        RunProcessAndFilter.filters = filters;
        RunProcessAndFilter.bufferSaveCode = bufferSaveCode;
    }

    private void setAllNameMethod(String name, ArrayList<String> arrayList) {
        CompilationUnit compilationUnit;
        StringBuilder stringBuilder = new StringBuilder();

        for (String s : arrayList) {
            String theCode = "public class Test {\n" + s + "\n}\n";
            try {
                compilationUnit = StaticJavaParser.parse(theCode);
                compilationUnit.findFirst(MethodDeclaration.class).get().setName(name);
            } catch (Exception ignore) { continue; }
            theCode = compilationUnit.toString();
            theCode = theCode.substring(21, theCode.length() - 3);
            stringBuilder.append(theCode);
            stringBuilder.append("\n\n");
        }

        bufferSaveCode.add(new CodeModel(name, stringBuilder.toString()));
    }

    private void processMethods() {
        GetAllMethods getAllMethods;
        try { getAllMethods = new GetAllMethods(this.str); }
        catch (ParseProblemException e) { return; }
        ArrayList<String> arrayList = getAllMethods.getMethods();
        CompilationUnit compilationUnit;

        for(String code : arrayList) {
            System.out.print("Processing: "+ animationChars[count] + "\r");
            count = (count + 1) % 4;

            if(code != null && !code.equals("null")) {
                String theCode = "public class Test {\n" + code + "\n}\n";
                String name;
                try {
                    compilationUnit = StaticJavaParser.parse(theCode);
                    name = compilationUnit.findFirst(MethodDeclaration.class).get().getNameAsString();
                }
                catch (Exception ignore) { continue; }


                for(String filter : filters) {
                    if(name.toLowerCase().contains(filter.toLowerCase())) {
                        setAllNameMethod(name, arrayList);
                        return;
                    }
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
