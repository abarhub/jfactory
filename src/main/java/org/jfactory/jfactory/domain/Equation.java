package org.jfactory.jfactory.domain;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Equation {

    private final List<Addition> additions;
    private final String valeur;
    private final List<Constante> valeurs;

    public Equation(List<Addition> additions, String valeur, List<Constante> valeurs) {
        this.additions = List.copyOf(additions);
        this.valeur = valeur;
        this.valeurs = List.copyOf(valeurs);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Equation.class.getSimpleName() + "[", "]")
                .add("additions=" + additions)
                .add("valeur='" + valeur + "'")
                .add("valeurs=" + valeurs)
                .toString();
    }
}
