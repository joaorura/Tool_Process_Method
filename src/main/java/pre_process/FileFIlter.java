package pre_process;

import java.io.File;
import java.io.FilenameFilter;

class FileFilter implements FilenameFilter {
    private String[] filters;

    public FileFilter(String[] filters) {
        this.filters = filters;
    }

    public boolean accept(File dir, String name) {
        boolean test = true;
        for (String aux : this.filters) {
            if (!name.toLowerCase().contains(aux) && name.contains(".")) {
               test = false;
               break;
            }
        }

        return test;
    }
}
