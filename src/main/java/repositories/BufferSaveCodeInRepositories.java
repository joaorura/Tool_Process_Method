package repositories;

import code_models.CodeModel;
import utils.BufferSaveCode;

import java.util.concurrent.ThreadLocalRandom;

public class BufferSaveCodeInRepositories extends BufferSaveCode {
    private long count = 0;

    private int[] percents, actualCounts;

    private String[] dataset;


    public BufferSaveCodeInRepositories(int size, String path, String[] dataset, int[] percents) {
        super(size, path);
        this.dataset = dataset;
        this.percents = percents;
        this.actualCounts = new int[] { 0 };
    }

    @Override
    protected String getNameOfClass(CodeModel element) {
        return element.result;
    }

    @Override
    protected String getNewPath(String nameOfClass) {
        int sorted;
        double percent;
        do {
            sorted = ThreadLocalRandom.current().nextInt(0, this.percents.length);
            percent = ((double) this.actualCounts[sorted] / this.count);
        } while (percent > this.percents[sorted]);


        this.count += 1;
        this.actualCounts[sorted] += 1;

        return super.path + "/" + this.dataset[sorted] + "/" + nameOfClass + ".java";
    }
}
