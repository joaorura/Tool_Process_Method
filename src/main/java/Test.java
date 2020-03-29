import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.Statement;

import java.util.List;

public class Test {

    public static void main(String[] args) {
        CompilationUnit compilationUnit = StaticJavaParser.parse("public class Test {\nprivate static int fifthPowerDigitSum(int x) {\n    int sum = 0;\n    while (x != 0) {\n        int y = x % 10;\n        sum += y * y * y * y * y;\n        x /= 10;\n    }\n    return sum;\n}\n}\n");
        compilationUnit.findAll(Statement.class).forEach(c -> {
            CompilationUnit compilationUnitNewCode = compilationUnit.clone();

            System.out.println("A:\n" + compilationUnit.toString() + "\nChange:\n" + c.toString() + "\nNew:" + compilationUnitNewCode.toString());
        });
    }
}
