package org.jfactory.jfactory.domain;

import java.math.BigInteger;
import java.util.Optional;

public class EquationSimple {

    private final Optional<Integer> a;

    private final Optional<Integer> b;

    private final Optional<Integer> c;

    private final int n;

    private final BigInteger rest;
    private final long rest2;

    public EquationSimple(Optional<Integer> a, Optional<Integer> b, Optional<Integer> c, int n, BigInteger rest, long rest2) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.n = n;
        this.rest = rest;
        this.rest2 = rest2;
    }

    public Optional<Integer> getA() {
        return a;
    }

    public Optional<Integer> getB() {
        return b;
    }

    public Optional<Integer> getC() {
        return c;
    }

    public int getN() {
        return n;
    }

    public BigInteger getRest() {
        return rest;
    }

    public long getRest2() {
        return rest2;
    }
}
