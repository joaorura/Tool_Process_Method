package utils;

public abstract class StrRunnable implements Runnable {
    protected String str;

    public StrRunnable(String str) {
        this.str = str;
    }
}
