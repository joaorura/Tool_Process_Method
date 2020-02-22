import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import pre_process.FileLister;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Initial path: " + args[0]);
        FileLister fileLister = new FileLister(args[0], new String[]{".java"}, true);
        ArrayList<File> files = fileLister.getFiles();
        BufferedReader bf;
        String line;
        StringBuilder text;
        CompilationUnit compilationUnit;

        for (File aux : files) {
            bf = new BufferedReader(new FileReader(aux));
            line = bf.readLine();
            text = new StringBuilder();

            while(line != null) {
                text.append(line).append("\n");
                line = bf.readLine();
            }

//            compilationUnit = new StaticJavaParser(text);
            System.out.println(text);
            break;
        }
    }
}
