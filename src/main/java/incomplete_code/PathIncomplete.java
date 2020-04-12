package incomplete_code;

import repositories.BufferSaveCodeInRepositories;
import utils.BufferFile;
import utils.BufferSaveCode;
import interaction_user.InterfaceProcess;

import static java.lang.Integer.parseInt;
import static pre_process.FileLister.createFileLister;

public class PathIncomplete implements InterfaceProcess {
    private int[] percents;

    private String pathRead;

    private String[] datasets;

    private BufferSaveCode bufferSaveCode;

    private BufferFile bufferFile;

    public PathIncomplete(int sizeOfBuffer, int numOfThreads, String pathRead, String pathWrite, String[] datasets, int[] percents) {
        this.pathRead = pathRead;
        this.datasets = datasets;
        this.percents = percents;

        this.bufferSaveCode = new BufferSaveCode(sizeOfBuffer, pathWrite);
        RunProcessIncomplete.setAll(bufferSaveCode);

        this.bufferFile = new BufferFile(sizeOfBuffer, RunProcessIncomplete.class, numOfThreads, this.bufferSaveCode);
    }

    public PathIncomplete(String[] args) throws IndexOutOfBoundsException {
        int sizeOfBuffer = parseInt(args[0]);
        int numOfThreads = parseInt(args[1]);

        this.pathRead = args[2];
        String pathWrite = args[3];

        this.datasets = args[4].split(", ");

        String[] parts = args[5].split(", ");
        this.percents = new int[parts.length];
        for(int i = 0; i < parts.length; i++) {
            this.percents[i] = parseInt(parts[i]);
        }

        this.bufferSaveCode = new BufferSaveCodeInRepositories(sizeOfBuffer, pathWrite, this.datasets, this.percents);
        RunProcessIncomplete.setAll(bufferSaveCode);

        this.bufferFile = new BufferFile(sizeOfBuffer, RunProcessIncomplete.class, numOfThreads, this.bufferSaveCode);
    }

    @Override
    public void process() {
        createFileLister(this.pathRead, this.bufferFile, bufferSaveCode);
    }
}
