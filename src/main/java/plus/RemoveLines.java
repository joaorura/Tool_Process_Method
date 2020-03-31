package plus;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import utils.Utils;


public class RemoveLines extends Remover {
    public RemoveLines(String algorithmName, String theCode, int limit) {
        super(algorithmName, theCode, limit);
    }

    @Override
    void remove(String code) {
        String[] lines = code.split("\\r?\\n");
        String momentCode;

        for(String line : lines) {
            System.out.print("Processing: " + ((float) Remover.amount / Remover.allAmount) * 100 + "%" + " " + Utils.animationChars[time] + "\r");
            super.time += 1;
            super.time %= 4;

            momentCode = code.replace(line + "\n", "");

            try {
                StaticJavaParser.parse("public class Test {\n" + momentCode + "\n}\n");
            }
            catch (ParseProblemException e) {
                continue;
            }

            super.linkedList.add(momentCode);
            try {
                remove(momentCode);
            }
            catch (StackOverflowError ignored) {
            }
        }
    }
}
