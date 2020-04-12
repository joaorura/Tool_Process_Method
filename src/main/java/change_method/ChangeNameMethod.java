package change_method;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class ChangeNameMethod {
    private String code;
    private String newNameMethod;
    private String newCode = null;

    public ChangeNameMethod(String code, String newNameMethod) {
        this.code = code;
        this.newNameMethod = newNameMethod;
    }

    public String getNewCode() {

        if (newCode == null) {
            CompilationUnit compilationUnit = StaticJavaParser.parse(code);
            compilationUnit.findAll(MethodDeclaration.class).forEach(c -> c.setName(newNameMethod));

            this.newCode = compilationUnit.toString();
        }

        return newCode;
    }
}
