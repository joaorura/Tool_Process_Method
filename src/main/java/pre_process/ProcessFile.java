package pre_process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ProcessFile {
    private File file;
    private String strFile = null;

    public ProcessFile(File file) {
        this.file = file;
    }

    public String getStringOfFile() throws IOException {
        if(strFile == null) {
            BufferedReader bf = new BufferedReader(new FileReader(this.file));
            String line = bf.readLine();
            StringBuilder text = new StringBuilder();

            while(line != null) {
                text.append(line).append("\n");
                line = bf.readLine();
            }

            this.strFile = text.toString();
        }

        return this.strFile;
    }
}
