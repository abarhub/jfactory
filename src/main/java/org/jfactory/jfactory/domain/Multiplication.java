package org.jfactory.jfactory.domain;

public class Multiplication implements Operation {

    private final Variable v1;
    private final Variable v2;

    public Multiplication(Variable v1, Variable v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public String toString() {
        return v1 + "*" + v2;
    }

    public Variable getV1() {
        return v1;
    }

    public Variable getV2() {
        return v2;
    }
}
