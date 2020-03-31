package plus;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import utils.Utils;


public class RemoveBlocks extends Remover {
    public RemoveBlocks(String algorithmName, String theCode, int limit) {
        super(algorithmName, theCode, limit);
    }

    @Override
    void remove(String code) {
        if(code.equals("") || super.time >= super.limit) { return; }

        try {
            System.out.print("Processing: " + ((float) Remover.amount / Remover.allAmount) * 100 + "%" + " " + Utils.animationChars[time % 4] + "\r");
            CompilationUnit compilationUnitCode = StaticJavaParser.parse(code);
            super.time += 1;
            compilationUnitCode.findAll(Statement.class).forEach(c -> {
                if(c.getClass().equals(BlockStmt.class)) { return; }
                CompilationUnit compilationUnitNewCode = compilationUnitCode.clone();
                compilationUnitNewCode.findAll(c.getClass()).forEach(b -> {
                    if(b.equals(c)) { b.remove(); }
                });
                String newCode = compilationUnitNewCode.toString();
                StaticJavaParser.parse(newCode);
                this.linkedList.add(newCode);
                try {
                    remove(newCode);
                }
                catch (StackOverflowError ignored) { }
            });
        }
        catch (ParseProblemException ignored) {}
    }
}
