package plus;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.Statement;

import java.util.LinkedList;

public class RemoveBlocks extends Remover {
    public RemoveBlocks(String theCode, String algorithmName) {
        super(theCode, algorithmName);
    }

    @Override
    void remove(String code) {
        try {
            System.out.print("Processing: " + super.animationChars[time] + "\r");
            super.time += 1;
            super.time %= 4;
            System.out.print("Processing: " + ((float) Remover.amount / Remover.allAmount) * 100 + "%" + " " + super.animationChars[time] + "\r");
            CompilationUnit compilationUnitCode = StaticJavaParser.parse(code);
            compilationUnitCode.findAll(Statement.class).forEach(c -> {
                CompilationUnit compilationUnitNewCode = compilationUnitCode.clone();
                compilationUnitNewCode.findAll(c.getClass()).forEach(b -> {
                    if(b.equals(c)) { b.remove(); }
                });
                String newCode = compilationUnitNewCode.toString();

                try {
                    StaticJavaParser.parse(newCode);
                    this.linkedList.add(newCode);
                    remove(newCode);
                    LinkedList<String> aux = new RemoveLines(newCode, this.algorithmName).call().getValue1();
                    this.linkedList.addAll(aux);
                }
                catch (ParseProblemException ignored) { }
                catch (StackOverflowError e) {
                    try { remove(super.linkedList.getLast()); }
                    catch (StackOverflowError ignored) { }
                }
            });
        }
        catch (ParseProblemException ignored) {}
    }
}
