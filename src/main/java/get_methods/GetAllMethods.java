package get_methods;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

public class GetAllMethods {
    private CompilationUnit compilationUnit;
    private HashMap<String, LinkedList<String>> mapAlgorithm = new HashMap<>();
    private HashMap<String, String> mapMethods = new HashMap<>();
    private HashMap<String, String> answer = null;

    public GetAllMethods(String text) throws ParseProblemException {
        this.compilationUnit = StaticJavaParser.parse(String.valueOf(text));
    }

    public HashMap<String, String> getAnswer() throws Exception {
        if(this.answer == null) {
            compilationUnit.findAll(MethodDeclaration.class).forEach(c -> {
                String method = c.getNameAsString();
                String code = c.toString();
                this.mapMethods.put(method, code);

                LinkedList<String> arrayList = new LinkedList<>();
                this.mapAlgorithm.put(method, arrayList);

                c.findAll(MethodCallExpr.class).forEach(d -> {
                    String scope = d.getScope().toString(), name = d.getNameAsString();

                    if(scope.equals("Optional.empty") && !arrayList.contains(name)) {
                        arrayList.add(name);
                    }
                });
            });

            this.answer = new HashMap<>();
            for (Map.Entry<String, LinkedList<String>> pair : mapAlgorithm.entrySet()) {
                StringBuilder stringBuilder = new StringBuilder();
                String str_aux;

                for(String method : pair.getValue()) {
                    str_aux = this.mapMethods.get(method);
                    stringBuilder.append(str_aux).append("\n");
                }

                str_aux = this.mapMethods.get(pair.getKey());
                stringBuilder.append(str_aux);
                str_aux = "public class " + pair.getKey() + " {\n" + stringBuilder.toString() + "\n}\n";

                answer.put(pair.getKey(), str_aux);
            }

            this.mapMethods.clear();
            this.mapAlgorithm.clear();
        }

        return this.answer;
    }
}
