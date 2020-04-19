package get_methods;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetAllMethods {
    private CompilationUnit compilationUnit;
    private HashMap<String, ArrayList<String>> mapAlgorithm = new HashMap<>();
    private HashMap<String, String> mapMethods = new HashMap<>();
    private ArrayList<String> answer = null;

    public GetAllMethods(String text) throws ParseProblemException {
        this.compilationUnit = StaticJavaParser.parse(String.valueOf(text));
    }

    public ArrayList<String> getMethods() {
        if(this.answer == null) {
            try {
                this.answer = new ArrayList<>();
                compilationUnit.findAll(MethodDeclaration.class).forEach(c -> {
                    String method = c.getNameAsString();
                    String code = c.toString();
                    this.mapMethods.put(method, code);

                    ArrayList<String> arrayList = new ArrayList<>();
                    this.mapAlgorithm.put(method, arrayList);

                    c.findAll(MethodCallExpr.class).forEach(d -> {
                        String scope = d.getScope().toString(), name = d.getNameAsString();

                        if(scope.equals("Optional.empty") && !arrayList.contains(name)) {
                            arrayList.add(name);
                        }
                    });
                });
            }
            catch (Exception e) { return  this.answer; }


            for (Map.Entry<String, ArrayList<String>> pair : mapAlgorithm.entrySet()) {
                StringBuilder stringBuilder = new StringBuilder();
                String str_aux;

                for(String method : pair.getValue()) {
                    str_aux = this.mapMethods.get(method);
                    stringBuilder.append(str_aux).append("\n");
                }

                str_aux = this.mapMethods.get(pair.getKey());
                stringBuilder.append(str_aux);

                answer.add(stringBuilder.toString());
            }
        }

        return this.answer;
    }
}
