package plus;

import org.javatuples.Pair;

import java.util.LinkedList;
import java.util.concurrent.Callable;

public abstract class Remover implements Callable<Pair<String, LinkedList<String>>> {
    static int amount = 0;
    static int allAmount;
    LinkedList<String> linkedList = null;
    String theCode;
    String algorithmName;
    char[] animationChars = new char[]{'|', '/', '-', '\\'};
    int time = 0;

    public Remover(String theCode, String algorithmName) {
        this.theCode = theCode;
        this.algorithmName = algorithmName;
    }

    abstract void remove(String code);

    @Override
    public Pair<String, LinkedList<String>> call() {
        if(linkedList == null) {
            linkedList = new LinkedList<>();
            remove("public class Test {\n" + this.theCode + "\n}\n");
        }

        amount += 1;
        return new Pair<>(this.algorithmName, linkedList);
    }

    public static void setAllAmount(int allAmount) {
        Remover.allAmount = allAmount;
    }
}
