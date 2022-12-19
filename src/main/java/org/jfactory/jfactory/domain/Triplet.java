package org.jfactory.jfactory.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class Triplet {

    private final int a;
    private final int b;

    private final int c;

    public Triplet(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getC() {
        return c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet triplet = (Triplet) o;
        return a == triplet.a && b == triplet.b && c == triplet.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Triplet.class.getSimpleName() + "[", "]")
                .add("a=" + a)
                .add("b=" + b)
                .add("c=" + c)
                .toString();
    }
}
