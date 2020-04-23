package utils;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class CodeUtils {
    public static String removeFirstClass(String str) {
        String newStr = str.replaceFirst("public *class *[^\\n]* *\\{ *\\n*", "");
        newStr = newStr.replaceFirst("\\n*}\\n*$", "");

        return newStr;
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
}
