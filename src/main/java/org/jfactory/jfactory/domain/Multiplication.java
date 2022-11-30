package org.jfactory.jfactory.domain;

import java.util.StringJoiner;

public class Multiplication {

    private final Variable v1;
    private final Variable v2;

    public Multiplication(Variable v1, Variable v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public String toString() {
        return v1+"*"+v2;
    }
}
