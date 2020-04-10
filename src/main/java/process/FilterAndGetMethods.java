package process;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import plus.json_models.CodeModel;
import pre_process.FileLister;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static utils.Utils.animationChars;
import static utils.Utils.createJson;

public class FilterAndGetMethods {
    private int count = 0;
    private int size;

    private String pathFile;
    private String[] filters;

    public final ArrayList<String> examples = new ArrayList<>();

    private BufferSaveCode bufferSaveCode;

    public FilterAndGetMethods(String pathFile, String[] filters, String path, int size) {
        this.pathFile = pathFile;
        this.filters = filters;

        this.bufferSaveCode = new BufferSaveCode(size, path, examples);
    }

    private class Run implements Callable<Void> {
        private String str;

        public Run(String str) {
            this.str = str;
        }


        private void setAllNameMethod(String name, ArrayList<String> arrayList) {
            for(int i = 0; i <= arrayList.size(); i++) {
                String theCode = "public class Test {\n" + arrayList.get(i) + "\n}\n";
                CompilationUnit compilationUnit = StaticJavaParser.parse(theCode);
                compilationUnit.findFirst(MethodDeclaration.class).get().setName(name);
                theCode = compilationUnit.toString().substring(21, arrayList.size() - 3);
                bufferSaveCode.add(new CodeModel(name, theCode));
            }
        }

        private void processMethods() {
            GetAllMethods getAllMethods = new GetAllMethods(this.str);
            ArrayList<String> arrayList = getAllMethods.getAlgorithms();

            for(String code : arrayList) {
                String theCode = "public class Test {\n" + code + "\n}\n";
                CompilationUnit compilationUnit = StaticJavaParser.parse(theCode);
                String name = compilationUnit.findFirst(MethodDeclaration.class).get().getNameAsString();

                for(String filter : filters) {
                    if(name.contains(filter)) {
                        setAllNameMethod(name, arrayList);
                        return;
                    }
                }
            }

            float percent = ((float) count /  size) * 100;
            System.out.print("Processing: " + percent + "% " + animationChars[count] + "\r");
            count += 1;
            count %= 4;
        }


        @Override
        public Void call() {
            processMethods();
            return null;
        }
    }


    public void process() {
        FileLister fileLister = new FileLister(this.pathFile, new String[]{".java"}, true);
        ArrayList<File> files = fileLister.getFiles();
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(64);
        LinkedList<Run> linkedList = new LinkedList<>();
        this.size = files.size();

        for (File file : files) {
            try {
                String str = new FileProcess(file).process();
                linkedList.add(new Run(str));
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        try {
            executorService.invokeAll(linkedList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.bufferSaveCode.process();
        executorService.shutdown();
    }
}
