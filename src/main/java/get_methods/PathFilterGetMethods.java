package get_methods;

import interaction_user.InterfaceProcess;

import static java.lang.Integer.parseInt;
import static utils.Utils.createJson;
import static utils.Utils.saveInFile;

public class PathFilterGetMethods implements InterfaceProcess {
    private int numberOfBuffer, numberOfThreads;

    private String pathRead, pathWrite, pathWriteJson;

    private String[] filters;

    public PathFilterGetMethods(int numberOfBuffer, int numberOfThreads, String pathRead, String pathWrite, String pathWriteJson, String[] filters) {
        this.numberOfBuffer = numberOfBuffer;
        this.numberOfThreads = numberOfThreads;
        this.pathRead = pathRead;
        this.pathWrite = pathWrite;
        this.pathWriteJson = pathWriteJson;
        this.filters = filters;
    }

    public PathFilterGetMethods(String[] args) throws IndexOutOfBoundsException {
        this.numberOfBuffer = parseInt(args[0]);
        this.numberOfThreads = parseInt(args[1]);
        this.pathRead = args[2];
        this.pathWrite = args[3];
        this.pathWrite = args[4];
        this.filters = args[5].split(", ");
    }

    private void processFilterAndGetMethods() {
        FilterAndGetMethods filterAndGetMethods = new FilterAndGetMethods(this.pathRead, filters, this.pathWrite, this.numberOfBuffer, this.numberOfThreads);
        filterAndGetMethods.process();

        saveInFile(this.pathWriteJson, createJson(filterAndGetMethods.examples));
    }


    @Override
    public void process() {
        this.processFilterAndGetMethods();
    }
}
