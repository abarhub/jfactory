package org.jfactory.jfactory.domain;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Addition {
    private final List<Operation> addition;
    private final int ordre;
    private final int valeur;

    public Addition(List<Operation> addition, int ordre, int valeur) {
        this.addition = List.copyOf(addition);
        this.ordre = ordre;
        this.valeur = valeur;
    }

    public List<Operation> getAddition() {
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
        return addition.stream().map(Operation::toString)
                .collect(Collectors.joining("+")) +
                "=" + valeur + "(" + ordre + ")";
    }
}
