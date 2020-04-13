package utils;

import java.util.ArrayList;

public abstract class ListBuffer<T> extends ArrayList<T> {
    public int theSize;

    public boolean lock = false;

    public ListBuffer(int size) {
        this.theSize = size;
    }

    public ListBuffer() { this(0); }

    public abstract boolean process();

    @Override
    public boolean add(T t) {
        if(!this.lock && this.theSize != 0 && super.size() == this.theSize) {
            super.add(t);
            return process();
        }
        else {
            return super.add(t);
        }
    }
}
