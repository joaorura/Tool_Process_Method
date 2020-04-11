package pre_process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileLister {
    private String dir;

    private String[] filters;

    private boolean recursive;

    public List<File> files;

    public FileLister(String dir, String[] filters, List<File> files, boolean recursive) {
        this.dir = dir;
        this.filters = filters;
        this.recursive = recursive;
        this.files = files;
    }

    private boolean checkFilters(File file) {
        for (String aux : this.filters) {
            if (!file.toString().endsWith(aux)) {
                return false;
            }
        }

        return true;
    }

    private void processDir(File dir) {
        File [] list = dir.listFiles();
        assert list != null;

        for (File elem : list) {
            if (elem.isDirectory() && this.recursive) {
                processDir(elem);
            } else if(checkFilters(elem)) {
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

    public void processFiles() {
        this.listFiles();
    }
}
