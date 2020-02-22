package pre_process;

import java.io.File;
import java.util.ArrayList;

public class FileLister {
    private String dir;
    private FileFilter filter;
    private boolean recursive;
    private ArrayList<File> files = new ArrayList<File>();

    public FileLister(String dir, String[] filters, boolean recursive) {
        this.dir = dir;
        this.filter = new FileFilter(filters);
        this.recursive = recursive;
    }

    private void processDir(File dir) {
        File [] list = dir.listFiles(this.filter);
        assert list != null;

        for (File elem : list) {
            if (elem.isDirectory()) {
                if (this.recursive) {
                    processDir(elem);
                }
            } else {
                this.files.add(elem);
            }
        }
    }

    private void listFiles() {
        File d = new File(this.dir);
        if (d.exists() && d.isDirectory()) {
           this.processDir(d);
        }
        else {
            throw new RuntimeException();
        }
    }

    public ArrayList<File> getFiles() {
        if (files.size() == 0) {
            this.listFiles();
        }

        return files;
    }
}
