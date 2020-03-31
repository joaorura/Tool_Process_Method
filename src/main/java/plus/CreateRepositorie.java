package plus;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import utils.Utils;

import java.io.File;
import java.util.*;

import static utils.Utils.saveInFile;

public class CreateRepositorie {
    private Map<String, List<String>> map;
    private File[] files;
    private int amount = 0, allAmount;


    public CreateRepositorie(Map<String, List<String>> map, String pathToSave) {
        this.map = map;
        this.files = new File[]{new File(pathToSave + "\\my_test_dir"), new File(pathToSave + "\\my_val_dir"), new File(pathToSave + "\\my_train_dir")};
    }

    private void saveFile(String code, String file) {
        CompilationUnit compilationUnit = StaticJavaParser.parse(code);
        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = compilationUnit.findFirst(ClassOrInterfaceDeclaration.class).get();
        String newName = classOrInterfaceDeclaration.getName().toString() + amount;
        classOrInterfaceDeclaration.setName(newName);
        String nameOfFile = "Code" +  newName + ".java";
        System.out.print("Processing: " + ((float) amount / allAmount) * 100 + "%" + " " + Utils.animationChars[amount % 4] + "\r");
        file += "\\" + nameOfFile;
        saveInFile(file, compilationUnit.toString());
        amount += 1;
    }

    public void process() {
        int size, i;
        List<String> list;
        for(File file : this.files) {
            file.mkdirs();
        }

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            allAmount += entry.getValue().size();
        }

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            list = entry.getValue();
            size = list.size();
            for (i = 0; i < size * 0.05; i++) saveFile(list.get(i), this.files[0].toString());
            for (; i < size * 0.20; i++) saveFile(list.get(i), this.files[1].toString());
            for(; i < size; i++) saveFile(list.get(i), this.files[2].toString());
        }
    }
}
