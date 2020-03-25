package plus;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static utils.Utils.saveInFile;

public class CreateRepositorie {
    private HashMap<String, ArrayList<String>> hashMap;
    private String pathToSave;

    public CreateRepositorie(HashMap<String, ArrayList<String>> hashMap, String pathToSave) {
        this.hashMap = hashMap;
        this.pathToSave = pathToSave;
    }

    public void process() {
        Random random = new Random();
        int count_test = 0, count_val = 0, size = 0;

        for (Map.Entry<String, ArrayList<String>> entry : hashMap.entrySet()) {
            size += entry.getValue().size();
        }

        for (Map.Entry<String, ArrayList<String>> entry : hashMap.entrySet()) {
            for(String code : entry.getValue()) {
                String nameOfFile = StaticJavaParser.parse(code).findFirst(ClassOrInterfaceDeclaration.class).get().getName() + ".java";
                String path = "";
                int test = random.nextInt(3);

                if (test == 0 && count_test < size * 0.05) {
                    count_test += 1;
                    path += pathToSave + "\\test\\" + nameOfFile;
                } else if(test == 1 && count_val < size * 0.15) {
                    count_val += 1;
                    path += pathToSave + "\\validate\\" + nameOfFile;
                } else {
                    path += pathToSave + "\\train\\" + nameOfFile;
                }

                saveInFile(path, code);
            }
        }
    }
}
