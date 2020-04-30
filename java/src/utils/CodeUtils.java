package utils;

import code_models.CodeModel;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeUtils {
    public static String removeFirstClass(String str)  {
        String newStr = str.replaceAll("((import)|(package))[^\\n]*\\n", "");
        newStr = newStr.replaceFirst(" *[^\\n]* *class *[^\\n]* *\\n*\\{ *\\n*", "");
        newStr = newStr.replaceFirst(" *\\n*} *\\n*$", "");

        return newStr;
    }

    public static boolean containAClass(String str) {
        Pattern pattern = Pattern.compile(" *[^\\n]* *class *[^\\n]* *\\n*\\{ *\\n*");
        Matcher matcher = pattern.matcher(str);
        return  matcher.lookingAt();
    }

    public static String getNameOfClass(String str) throws RuntimeException {
        String name;

        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(str);
            ClassOrInterfaceDeclaration classOrInterfaceDeclaration = compilationUnit.findFirst(ClassOrInterfaceDeclaration.class).get();
            name = classOrInterfaceDeclaration.getNameAsString();
        }
        catch (Exception e) { throw new RuntimeException("Error in get name of Class.", e); }

        return name;
    }

    public static String getNameOfClassFromFile (String str) throws RuntimeException {
        String[] splited  = str.split("/");
        return splited[splited.length - 1].replace(".java", "");
    }

    public static CodeModel setAllNameMethod(String name, String code) throws RuntimeException {

        CompilationUnit compilationUnit;

        try {
            compilationUnit = StaticJavaParser.parse(code);
            compilationUnit.findAll(MethodDeclaration.class).forEach(c -> c.setName(name));
        } catch (Exception ignore) { throw new RuntimeException(code); }

        String theCode = compilationUnit.toString();
        try {
            theCode = CodeUtils.removeFirstClass(theCode);
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return  new CodeModel(name, theCode);
    }
}
