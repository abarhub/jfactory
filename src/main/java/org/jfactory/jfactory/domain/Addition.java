package org.jfactory.jfactory.domain;

import java.util.List;
import java.util.StringJoiner;

public class Addition {
    private final List<Multiplication> addition;
    private final int ordre;
    private final int valeur;

    public Addition(List<Multiplication> addition, int ordre, int valeur) {
        this.addition = List.copyOf(addition);
        this.ordre = ordre;
        this.valeur = valeur;
    }

    public List<Multiplication> getAddition() {
        return addition;
    }

    public int getOrdre() {
        return ordre;
    }

    public int getValeur() {
        return valeur;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Addition.class.getSimpleName() + "[", "]")
                .add("addition=" + addition)
                .add("ordre=" + ordre)
                .add("valeur=" + valeur)
                .toString();
    }
}
