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
    int time = 0, limit;

    public Remover(String algorithmName, String theCode, int limit) {
        this.theCode = theCode;
        this.algorithmName = algorithmName;
        this.limit = limit;
    }

    abstract void remove(String code);

    @Override
    public Pair<String, LinkedList<String>> call() {
        if(linkedList == null) {
            linkedList = new LinkedList<>();
            remove(this.theCode);
        }

        amount += 1;
        return new Pair<>(this.algorithmName, linkedList);
    }

    public static void setAllAmount(int allAmount) {
        Remover.allAmount = allAmount;
    }
    public static void resetAmount() { Remover.amount = 0; }
}
