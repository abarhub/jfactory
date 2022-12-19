package org.jfactory.jfactory.domain;

public class Doublet {

    private final int x;
    private final int y;

    private final int size;

    public Doublet(int x, int y) {
        this.x = x;
        this.y = y;
        size = 2;
    }

    public Doublet(int x) {
        this.x = x;
        this.y = -1;
        size = 1;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
