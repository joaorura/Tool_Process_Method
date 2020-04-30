package repositories;

import code_models.CodeModel;
import utils.BufferSaveCode;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class BufferSaveCodeInRepositories extends BufferSaveCode {
    private long count = 0;

    private int[] actualCounts;

    private double[] percents;

    private String[] dataset;


    public BufferSaveCodeInRepositories(int size, String path, String[] dataset, double[] percents) throws RuntimeException{
        super(size, path);

        File file = new File(path);
        System.out.println(file);
        if(!file.exists() && !file.mkdirs()) {
            throw new RuntimeException();
        }

        this.dataset = dataset;
        for(String str: dataset) {
            file = new File(path + "/" + str);
            file.mkdirs();
        }

        this.percents = percents;

        this.actualCounts = new int[this.percents.length];
        Arrays.fill(actualCounts, 0);
    }

    @Override
    protected String getNameOfClass(CodeModel element) {
        return element.result;
    }

    @Override
    protected String getNewPath(String nameOfClass) {
        int sorted;
        double percent;
        this.count += 1;

        do {
            sorted = ThreadLocalRandom.current().nextInt(0, this.percents.length);
            percent = ((double) this.actualCounts[sorted] / this.count);
        } while (percent >= this.percents[sorted]);

        this.actualCounts[sorted] += 1;
        return super.path + "/" + this.dataset[sorted] + "/" + nameOfClass + ".java";
    }
}
