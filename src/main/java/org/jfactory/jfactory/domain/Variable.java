package org.jfactory.jfactory.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class Variable {
    private final String nom;
    private boolean affecte;
    private int valeur;

    private final int no;

    public Variable(String nom, int no) {
        this.nom = nom;
        this.no=no;
        affecte=false;
        valeur=-1;
    }

    public String getNom() {
        return nom;
    }

    public boolean isAffecte() {
        return affecte;
    }

    public void setAffecte(boolean affecte) {
        this.affecte = affecte;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public int getNo() {
        return no;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return affecte == variable.affecte && valeur == variable.valeur && Objects.equals(nom, variable.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, affecte, valeur);
    }

    @Override
    public String toString() {
        if(affecte){
            return ""+valeur;
        } else {
            return nom;
        }
    }
}
