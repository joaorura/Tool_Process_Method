package incomplete_code;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.javatuples.Pair;
import utils.Utils;

import java.util.LinkedList;
import java.util.concurrent.Callable;


public class RemoveBlocks implements Callable<Pair<String, LinkedList<String>>> {
    private static int allAmount, amount = 0;
    private int limit, time = 0;

    private LinkedList<String> linkedList = null;
    private String theCode;
    private String algorithmName;

    public RemoveBlocks(String algorithmName, String theCode, int limit) {
        this.theCode = theCode;
        this.algorithmName = algorithmName;
        this.limit = limit;
    }

    void remove(String code) {
        if(code.equals("")) { return; }

        try {
            System.out.print("Processing: " + ((float) RemoveBlocks.amount / RemoveBlocks.allAmount) * 100 + "%" + " " + Utils.animationChars[time % 4] + "\r");


            CompilationUnit compilationUnitCode = StaticJavaParser.parse(code);
            compilationUnitCode.findAll(Statement.class).forEach(c -> {
                if(this.time >= this.limit) { return; }

                if(c.getClass().equals(BlockStmt.class)) { return; }
                CompilationUnit compilationUnitNewCode = compilationUnitCode.clone();
                compilationUnitNewCode.findAll(c.getClass()).forEach(b -> {
                    if(b.equals(c)) { b.remove(); }
                });
                String newCode = compilationUnitNewCode.toString();
                StaticJavaParser.parse(newCode);
                this.time += 1;
                this.linkedList.add(newCode);
                try { remove(newCode); }
                catch (StackOverflowError ignored) { }
            });
        }
        catch (ParseProblemException ignored) {}
    }

    @Override
    public Pair<String, LinkedList<String>> call() {
        if(linkedList == null) {
            linkedList = new LinkedList<>();
            linkedList.add(theCode);
            remove(theCode);
        }

        amount += 1;
        return new Pair<>(this.algorithmName, linkedList);
    }

    public static void setAllAmount(int allAmount) {
        RemoveBlocks.allAmount = allAmount;
    }
    public static void resetAmount() { RemoveBlocks.amount = 0; }
}
