package process;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FilterCode {
    private CompilationUnit compilationUnit;
    private HashMap<String, ArrayList<String>> mapAlgorithm = new HashMap<>();
    private HashMap<String, String> mapMethods = new HashMap<>();
    private ArrayList<String> answer = null;

    static class MethodCallVisitor extends VoidVisitorAdapter<Void> {
        private ArrayList<String> arrayList;

        public MethodCallVisitor(ArrayList<String> arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public void visit(MethodCallExpr n, Void arg) {
            String scope = n.getScope().toString();
            String strAux;

            if (scope.equals("Optional.empty")) {
                strAux = n.getName().toString();
                if(!arrayList.contains(strAux)) {
                    arrayList.add(strAux);
                }
            }

            super.visit(n, arg);
        }
    }

    public FilterCode(String text) {
        this.compilationUnit = StaticJavaParser.parse(String.valueOf(text));
    }

    public ArrayList<String> getAlgorithms() {
        if(this.answer == null) {
            this.answer = new ArrayList<>();
            compilationUnit.findAll(MethodDeclaration.class).forEach(c -> {
                String method = c.getName().toString();
                String code = c.toString();

                this.mapMethods.put(method, code);
                ArrayList<String> arrayList = new ArrayList<>();
                this.mapAlgorithm.put(method, arrayList);

                c.accept(new MethodCallVisitor(arrayList), null);
            });

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

        return (ArrayList<String>) this.answer.clone();
    }
}
