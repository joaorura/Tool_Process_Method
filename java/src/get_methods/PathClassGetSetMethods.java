package get_methods;

import interaction_user.InterfaceProcess;
import pre_process.FileLister;
import utils.BufferFile;
import utils.BufferSaveCode;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;
import static utils.Utils.createJson;
import static utils.Utils.saveInFile;

public class PathClassGetSetMethods implements InterfaceProcess {
    private final ArrayList<String> examples = new ArrayList<>();

    private String pathRead, pathWriteJson;

    private BufferFile bufferFile;


    public PathClassGetSetMethods(int numberOfBuffer, int numberOfThreads, String pathRead, String pathWrite, String pathWriteJson) {
        this.pathRead = pathRead;
        this.pathWriteJson = pathWriteJson;

        BufferSaveCode bufferSaveCode = new BufferSaveCode(numberOfBuffer, pathWrite, this.examples);
        RunSetGetMethods.setAll(bufferSaveCode, examples);

        this.bufferFile = new BufferFile(numberOfBuffer, RunSetGetMethods.class, numberOfThreads, bufferSaveCode);
    }

    public PathClassGetSetMethods(String[] args) throws IndexOutOfBoundsException {
        this(parseInt(args[0]), parseInt(args[1]), args[2], args[3], args[4]);
    }


    public void process() {
        FileLister fileLister = new FileLister(this.pathRead, new String[]{".java"}, this.bufferFile, true);
        System.out.println("Executing");
        fileLister.processFiles();

        try {
            bufferFile.waitForExecution();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        saveInFile(this.pathWriteJson, createJson(this.examples));
    }

}
