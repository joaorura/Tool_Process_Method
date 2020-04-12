package repositories;

import incomplete_code.RunProcessIncomplete;
import interaction_user.InterfaceProcess;
import utils.BufferFile;
import utils.BufferSaveCode;

import static java.lang.Integer.parseInt;
import static pre_process.FileLister.createFileLister;

public class PathCreateRepositories implements InterfaceProcess {
    private String pathRead;

    private BufferSaveCode bufferSaveCode;

    private BufferFile bufferFile;

    public PathCreateRepositories(int sizeOfBuffer, int numOfThreads, String pathRead, String pathWrite, String[] datasets, int[] percents) {
        this.pathRead = pathRead;

        this.bufferSaveCode = new BufferSaveCodeInRepositories(sizeOfBuffer, pathWrite, datasets, percents);
        RunProcessIncomplete.setAll(bufferSaveCode);

        this.bufferFile = new BufferFile(sizeOfBuffer, RunProcessIncomplete.class, numOfThreads, this.bufferSaveCode);
    }

    public PathCreateRepositories(String[] args) throws IndexOutOfBoundsException {
        int sizeOfBuffer = parseInt(args[0]);
        int numOfThreads = parseInt(args[1]);

        this.pathRead = args[2];
        String pathWrite = args[3];

        String[] datasets = args[4].split(", ");

        String[] parts = args[5].split(", ");
        int[] percents = new int[parts.length];
        for(int i = 0; i < parts.length; i++) {
            percents[i] = parseInt(parts[i]);
        }

        this.bufferSaveCode = new BufferSaveCodeInRepositories(sizeOfBuffer, pathWrite, datasets, percents);
        RunProcessIncomplete.setAll(bufferSaveCode);

        this.bufferFile = new BufferFile(sizeOfBuffer, RunProcessIncomplete.class, numOfThreads, this.bufferSaveCode);
    }

    @Override
    public void process() {
        createFileLister(this.pathRead, this.bufferFile, bufferSaveCode);
    }
}
