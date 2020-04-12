package utils;

import java.util.ArrayList;

public abstract class ListBuffer<T> extends ArrayList<T> {
    public int size = 0;

    public boolean lock = false;

    public ListBuffer(int size) {
        this.size = size;
    }

    public ListBuffer() { this(0); }

    public abstract boolean process();

    @Override
    public boolean add(T t) {
        if(!this.lock && this.size == 0 && super.size() == this.size && super.add(t)) {
            return process();
        }
        else {
            return super.add(t);
        }
    }
}
