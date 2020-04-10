package process;


import pre_process.ProcessFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileProcess {
    private File file;
    private String text = null;
    public FileProcess(File file) {
        this.file = file;
    }

    public String process() {
        if(this.text == null) {
            ProcessFile theProcessFile = new ProcessFile(this.file);
            String strFile;
            try {
                strFile = theProcessFile.getStringOfFile();
                byte[] utf8 = strFile.getBytes(StandardCharsets.UTF_8);
                strFile = new String(utf8, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Error ao ler arquivo e converter para UTF-8");
            }

            this.text = strFile;
        }

        return this.text;
    }
}
