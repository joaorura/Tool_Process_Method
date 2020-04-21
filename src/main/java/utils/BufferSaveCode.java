package utils;

import code_models.CodeModel;

import java.io.File;
import java.util.ArrayList;

import static utils.Utils.saveInFile;

public class BufferSaveCode extends ListBuffer<CodeModel> {
    private int count = 0;

    private ArrayList<String> examples;

    private String nameOfClass;

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

    protected String getNewCode(String str) {
        return "public class " + this.nameOfClass + " {\n" +
                str +
                "\n}\n";
    }

    private void send(CodeModel element) {
        this.nameOfClass = getNameOfClass(element);
        String newPath = getNewPath(this.nameOfClass);
        String newCode = getNewCode(element.code);

        if(this.examples != null && !this.examples.contains(element.result)) {
            this.examples.add(element.result);
        }

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
