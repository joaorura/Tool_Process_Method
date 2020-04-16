package incomplete_code;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import org.javatuples.Pair;
import utils.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;


public class RemoveBlocks implements Callable<Pair<String, LinkedList<String>>> {
    private int limit, time = 0;

    private LinkedList<String> linkedList = null;
    private String theCode;
    private String algorithmName;

    public RemoveBlocks(String algorithmName, String theCode, int limit) {
        this.theCode = theCode;
        this.algorithmName = algorithmName;
        this.limit = limit;
    }

    private <T extends Node> void processCompilation(String code, Class<T> theClass) {
        if(this.time >= this.limit) { return; }
        LinkedList<String> auxList = new LinkedList<>();

        try {
            CompilationUnit compilationUnitCode = StaticJavaParser.parse(code);
            List<T> list = compilationUnitCode.findAll(theClass);

            for(T c : list) {
                System.out.print("Processing: " + Utils.animationChars[time % 4] + "\r");

                if(this.time >= this.limit) { break; }
                try {
                    CompilationUnit compilationUnitNewCode = compilationUnitCode.clone();
                    List<T> theList = compilationUnitNewCode.findAll(theClass);
                    for(T t : theList) {
                        if(t.equals(c)) {
                            t.remove();
                            break;
                        }
                    }

                    String newCode = compilationUnitNewCode.toString();
                    this.time += 1;
                    auxList.add(newCode);
                }
                catch (Exception ignored) { }
            }

            list.clear();

            for (int i = 0; i < auxList.size(); i++) {
                String theCode = auxList.get(0);
                auxList.remove(0);
                this.linkedList.add(theCode);
                this.remove(theCode);
            }
        }
        catch (Exception ignored) { }
    }

    private void remove(String code) {
        this.processCompilation(code, IfStmt.class);
        this.processCompilation(code, SwitchStmt.class);
        this.processCompilation(code, ForStmt.class);
        this.processCompilation(code, ForEachStmt.class);
        this.processCompilation(code, DoStmt.class);
        this.processCompilation(code, WhileStmt.class);
        this.processCompilation(code, ExpressionStmt.class);
    }

    @Override
    public Pair<String, LinkedList<String>> call() {
        if(this.linkedList == null) {
            this.linkedList = new LinkedList<>();
            this.linkedList.add(this.theCode);
            remove(this.theCode);
        }


        return new Pair<>(this.algorithmName, this.linkedList);
    }
}
