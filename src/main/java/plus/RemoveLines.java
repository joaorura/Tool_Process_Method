package plus;

import com.github.javaparser.StaticJavaParser;
import org.javatuples.Pair;

import java.util.LinkedList;
import java.util.concurrent.Callable;

public class RemoveLines implements Callable<Pair<String, LinkedList<String>>> {
    private LinkedList<String> arrayList = null;
    private String code;
    private String algorithmName;

    public RemoveLines(String code, String algorithmName) {
        this.code= code;
        this.algorithmName = algorithmName;
    }

    private void process() {
        String[] lines = this.code.split("\\r?\\n");
        String momentCode;
        System.out.println("Original Code: ");
        System.out.println("\t\t" + code);
        for(String line : lines) {
            momentCode = code.replace(line, "");
            System.out.println("Removed Code: ");
            System.out.println("\t\t" + momentCode);
            StaticJavaParser.parse(momentCode);
        }
    }

    @Override
    public Pair<String, LinkedList<String>> call() {
        if(arrayList == null) {
            this.process();
        }

        return new Pair<>(this.algorithmName, arrayList);
    }
}
