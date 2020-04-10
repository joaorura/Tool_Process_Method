package process;

import plus.json_models.CodeModel;

import java.util.ArrayList;
import java.util.LinkedList;

import static utils.Utils.saveInFile;

public class BufferSaveCode extends LinkedList<CodeModel> {
    private int size;
    private int count = 0;

    private ArrayList<String> examples;

    private String path;

    public BufferSaveCode(int size, String path, ArrayList<String> examples) {
        this.size = size;
        this.path = path;
        this.examples = examples;
    }

    private void send(CodeModel element) {
        String nameOfClass = element.result + count;
        String newPath = this.path + "\\" + nameOfClass + ".java";
        String newCode = "public class " + nameOfClass + " {\n" +
                element.code +
                "\n}\n";

        if(!this.examples.contains(element.result)) { this.examples.add(element.result); }
        saveInFile(newPath, newCode);
        count += 1;
    }

    @Override
    public boolean add(CodeModel codeModel) {
        if(super.size() == this.size && super.add(codeModel)) {
            for(int i = 0; i <= this.size; i++) {
                CodeModel element = super.get(i);
                send(element);
            }

            super.clear();
            return true;
        }
        else {
            return super.add(codeModel);
        }
    }
}
