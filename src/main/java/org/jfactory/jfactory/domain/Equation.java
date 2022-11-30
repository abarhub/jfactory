package org.jfactory.jfactory.domain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Equation {

    private final List<Addition> additions;
    private final String valeur;
    private final List<Constante> valeurs;

    private final BigInteger valeurBi;

    private final List<Variable> listeVariables;

    public Equation(List<Addition> additions, String valeur, List<Constante> valeurs) {
        this.additions = List.copyOf(additions);
        this.valeur = valeur;
        this.valeurs = List.copyOf(valeurs);
        valeurBi=new BigInteger(valeur);
        listeVariables=getListVariables(additions);
    }

    private List<Variable> getListVariables(List<Addition> additions) {
        List<Variable> liste=new ArrayList<>();
        return List.copyOf(liste);
    }

    public Addition getEquationSimplifiee(int ordre){
        List<Operation> liste=new ArrayList<>();
        var add=additions.get(ordre);
        var n=0;
        for(var i=0;i<add.getAddition().size();i++){
            var op=add.getAddition().get(i);
            if(op instanceof Multiplication mult){
                if(mult.getV1().isAffecte()&&mult.getV2().isAffecte()){
                    n+=mult.getV1().getValeur()*mult.getV2().getValeur();
                } else {
                    liste.add(mult);
                }
            } else if(op instanceof Constante cst){
                n+=cst.getValeur();
            }
        }
        if(n!=0){
            liste.add(new Constante(n));
        }
        return new Addition(liste,ordre,valeurs.get(ordre).getValeur());
    }

    public BigInteger calcul(int ordre){
        var res=BigInteger.ZERO;

        for(var i=0;((i<=ordre)||(ordre==-1)&&i< additions.size());i++){
            var add=additions.get(i);
            for(var m:add.getAddition()){
                if(m instanceof Multiplication mult){
                    if(mult.getV1().isAffecte()&&mult.getV2().isAffecte()){
                        var v1=BigInteger.valueOf(mult.getV1().getValeur());
                        var v2=BigInteger.valueOf(mult.getV2().getValeur());
                        var bi=v1.multiply(v2);
                        res=res.add(bi.multiply(BigInteger.TEN.pow(i)));
                    } else {
                        return BigInteger.valueOf(-1);
                    }
                } else if(m instanceof Constante cst){
                    res=res.add(BigInteger.valueOf(cst.getValeur()));
                }
            }
        }

        return res;
    }

    public boolean estValide(int ordre,boolean exacte){
        BigInteger res;
        if(exacte){
            res=calcul(-1);
        } else {
            res=calcul(ordre);
        }
        if(res.equals(BigInteger.valueOf(-1))){
            return false;
        }
        if(exacte){
            return res.equals(valeurBi);
        } else {
            return res.add(valeurBi.negate()).mod(BigInteger.TEN.pow(ordre)).equals(BigInteger.ZERO);
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Equation.class.getSimpleName() + "[", "]")
                .add("additions=" + additions)
                .add("valeur='" + valeur + "'")
                .add("valeurs=" + valeurs)
                .toString();
    }

    public List<Addition> getAdditions() {
        return additions;
    }

    public String getValeur() {
        return valeur;
    }

    public List<Constante> getValeurs() {
        return valeurs;
    }
}
