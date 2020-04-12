package utils;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class CodeUtils {
    public static String getNameOfCode(String str) throws RuntimeException {
        String name;

        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(str);
            name = compilationUnit.findFirst(ClassOrInterfaceDeclaration.class).get().getClass().getName();
        }
        catch (Exception e) { throw new RuntimeException("Error in get name of Class.", e); }

        return name;
    }
}
