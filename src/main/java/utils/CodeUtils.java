package utils;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class CodeUtils {
    public static String removeFirstClass(String str) {
        return str.replaceAll("(public *class *[A-z]*[0-9]* *\\{\\n*)|(}\\n*$)", "");
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
