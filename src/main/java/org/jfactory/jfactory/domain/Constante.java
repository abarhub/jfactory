package org.jfactory.jfactory.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class Constante {

    private final int valeur;

    public Constante(int valeur) {
        this.valeur = valeur;
    }

    public int getValeur() {
        return valeur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constante constante = (Constante) o;
        return valeur == constante.valeur;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valeur);
    }

    @Override
    public String toString() {
        return "" + valeur;
    }
}
