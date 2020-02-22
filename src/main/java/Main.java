import pre_process.FileLister;

import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initial path: " + args[0]);
        FileLister fileLister = new FileLister(args[0], new String[]{".java"}, true);
        ArrayList<File> files = fileLister.getFiles();
        System.out.println(files);
    }
}
