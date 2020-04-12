package get_methods;

import interaction_user.InterfaceProcess;
import pre_process.FileLister;
import pre_process.FileProcess;
import utils.ListBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import static utils.Utils.*;

public class JsonGetMethods implements InterfaceProcess {
    private LinkedList<String> algorithms = new LinkedList<>();

    private String pathRead, pathWrite;

    public JsonGetMethods(String pathRead, String pathWrite) {
        this.pathRead = pathRead;
        this.pathWrite = pathWrite;
    }

    public JsonGetMethods(String[] args) throws IndexOutOfBoundsException {
        this(args[0], args[1]);
    }

    public void processFiles(String path) {
        ListBuffer<File> files = new ListBuffer<File>() {
            @Override
            public boolean process() { return true; }
        };

        FileLister fileLister = new FileLister(path, new String[]{".java"}, files, true);
        fileLister.processFiles();
        float percent;
        int count = 0;

        for(int i = 0; i < files.size(); i++) {
            try {
                String str = new FileProcess(files.get(i)).process();
                GetAllMethods getAllMethods = new GetAllMethods(str);
                algorithms.addAll(getAllMethods.getMethods());
            }
            catch (RuntimeException e) {
                System.out.println(e.getMessage());
                count++;
            }
            percent = ((float) i /  files.size()) * 100;
            System.out.print("Processing: " + percent + "% " + animationChars[i % 4] + "\r");
        }

        System.out.println("Process Done!!");
        System.out.println("Porcentagem de arquivos com erros:" + (((float) count / files.size()) * 100) + "%");
        System.out.println("Numéro de métodos colétados: " + algorithms.size());
    }

    @Override
    public void process() {
        System.out.println("Initial path: " + this.pathRead);
        System.out.println("Data path: " +  this.pathWrite);

        processFiles(this.pathRead);

        try {
            String json = createJson(algorithms);
            saveInFile(this.pathWrite, json);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n\nError na cricao da String json ou no salvamente desta");
        }
    }
}
