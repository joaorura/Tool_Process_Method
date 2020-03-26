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

    private void saveFile(String code, String type) {
        String nameOfFile = StaticJavaParser.parse(code).findFirst(ClassOrInterfaceDeclaration.class).get().getName() + ".java";
        String path = pathToSave + "\\" + type + "\\" + nameOfFile;
        saveInFile(path, code);
    }

    public void process() {
        int size, i;
        ArrayList<String> arrayList;

        for (Map.Entry<String, ArrayList<String>> entry : hashMap.entrySet()) {
            arrayList = entry.getValue();
            size = arrayList.size();
            for (i = 0; i < size * 0.05; i++) saveFile(arrayList.get(i), "test");
            for (; i < size * 0.20; i++) saveFile(arrayList.get(i), "validate");
            for(; i < size; i++) saveFile(arrayList.get(i), "train");
        }
    }
}
