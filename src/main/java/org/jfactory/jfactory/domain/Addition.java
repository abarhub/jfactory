package org.jfactory.jfactory.domain;

import org.springframework.util.Assert;

import java.util.ArrayList;
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

    public List<Variable> getToutesVariables(){
        return getVariables(false);
    }

    public List<Variable> getVariablesNonAffectees(){
        return getVariables(true);
    }

    private List<Variable> getVariables(boolean nonAffecte){
        var liste=new ArrayList<Variable>();
        for(var elt:addition){
            if(elt instanceof Multiplication mult) {
                var v = mult.getV1();
                if (!v.isAffecte() || !nonAffecte) {
                    if (!liste.contains(v)) {
                        liste.add(v);
                    }
                }
                v = mult.getV2();
                if (!v.isAffecte() || !nonAffecte) {
                    if (!liste.contains(v)) {
                        liste.add(v);
                    }
                }
            } else if (elt instanceof Constante) {
                // rien a faire
            } else {
                Assert.state(false,"Operation non geree : "+elt);
            }
        }
        return liste;
    }
}
