package incomplete_code;

import utils.BufferFile;
import utils.BufferSaveCode;
import interaction_user.InterfaceProcess;

import static java.lang.Integer.parseInt;
import static pre_process.FileLister.createFileLister;

public class PathIncomplete implements InterfaceProcess {
    private String pathRead;

    private BufferSaveCode bufferSaveCode;

    private BufferFile bufferFile;

    public PathIncomplete(int sizeOfBuffer, int limitOfCode, int numOfThreads, String pathRead, String pathWrite) {
        this.pathRead = pathRead;

        this.bufferSaveCode = new BufferSaveCode(sizeOfBuffer, pathWrite);
        RunProcessIncomplete.setAll(this.bufferSaveCode, limitOfCode);

        this.bufferFile = new BufferFile(sizeOfBuffer, RunProcessIncomplete.class, numOfThreads, this.bufferSaveCode);
    }

    public PathIncomplete(String[] args) throws IndexOutOfBoundsException {
        int sizeOfBuffer = parseInt(args[0]);
        int limitOfCode = parseInt(args[1]);
        int numOfThreads = parseInt(args[2]);
        this.pathRead = args[3];
        String pathWrite = args[4];

        this.bufferSaveCode = new BufferSaveCode(sizeOfBuffer, pathWrite);
        RunProcessIncomplete.setAll(this.bufferSaveCode, limitOfCode);

        this.bufferFile = new BufferFile(sizeOfBuffer, RunProcessIncomplete.class, numOfThreads, this.bufferSaveCode);
    }

    @Override
    public void process() {
        createFileLister(this.pathRead, this.bufferFile, this.bufferSaveCode);
    }
}
