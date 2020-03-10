package pre_process;

import java.io.File;
import java.util.ArrayList;

public class FileLister {
    private String dir;
    private String[] filters;
    private boolean recursive;
    private ArrayList<File> files = new ArrayList<>();

    public FileLister(String dir, String[] filters, boolean recursive) {
        this.dir = dir;
        this.filters = filters;
        this.recursive = recursive;
    }

    private boolean checkFilters(File file) {
        for (String aux : this.filters) {
            if (!file.toString().contains(aux)) {
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

    public ArrayList<File> getFiles() {
        if (files.size() == 0) {
            this.listFiles();
        }

        return files;
    }
}
