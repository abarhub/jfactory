package org.jfactory.jfactory.domain;

public class Doublet {

    private final int x;
    private final int y;

    private final int size;

    private final long id;

    public Doublet(int x, int y, long id) {
        this.x = x;
        this.y = y;
        this.id = id;
        size = 2;
    }

    public Doublet(int x, long id) {
        this.x = x;
        this.y = -1;
        this.id = id;
        size = 1;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public long getId() {
        return id;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        if (size == 2) {
            return "x=" + x + ",y=" + y;
        } else {
            return "x=" + x;
        }
    }
}
