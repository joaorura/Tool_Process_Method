package utils;

import code_models.CodeModel;

import java.io.File;
import java.util.ArrayList;

import static utils.Utils.saveInFile;

public class BufferSaveCode extends ListBuffer<CodeModel> {
    private int count = 0;

    private ArrayList<String> examples;

    protected String path;

    public BufferSaveCode(int size, String path, ArrayList<String> examples) {
        super(size);
        this.path = path;
        new File(path).mkdirs();
        this.examples = examples;
    }

    public BufferSaveCode(int size, String path) { this(size, path, null); }

    protected String getNameOfClass(CodeModel element) {
        return element.result.toUpperCase() + count;
    }

    protected String getNewPath(String nameOfClass) {
        return this.path + "/" + nameOfClass + ".java";
    }

    private void send(CodeModel element) {
        String nameOfClass = getNameOfClass(element);
        String newPath = getNewPath(nameOfClass);
        String newCode = "public class " + nameOfClass + " {\n" +
                element.code +
                "\n}\n";

        if(this.examples != null && !this.examples.contains(element.result)) {
            this.examples.add(element.result);
        }

        System.out.println(newPath);
        saveInFile(newPath, newCode);
        count += 1;
    }

    public boolean process() {
        super.lock = true;

        for(int i = 0; i < super.size(); i++) {
            try {
                CodeModel codeModel = super.get(0);
                super.remove(0);
                send(codeModel);
            }
            catch (Exception ignore) {
                super.lock = false;
                return false;
            }
        }

        super.lock = false;
        return true;
    }
}
