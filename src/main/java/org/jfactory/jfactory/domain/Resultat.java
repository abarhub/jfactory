package org.jfactory.jfactory.domain;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.List;
import java.util.StringJoiner;

public class Resultat {

    private final List<Variable> listeVariables;

    private final List<Constante> valeurs;

    private final BigInteger valeurBi;

    public Resultat(List<Variable> listeVariables, List<Constante> valeurs, BigInteger valeurBi) {
        this.listeVariables = listeVariables;
        this.valeurs = valeurs;
        this.valeurBi = valeurBi;
    }

    @Override
    public String toString() {
        StringBuilder str=new StringBuilder();
        for(var tmp:listeVariables){
            if(!str.isEmpty()){
                str.append(',');
            }
            str.append(tmp.getNom()).append('=').append(tmp.getValeur());
        }
        return new StringJoiner(", ", Resultat.class.getSimpleName() + "[", "]")
                .add("listeVariables:" + str)
                .add("valeurs:" + valeurs)
                .add("valeurBi:" + valeurBi+"="+getX()+"*"+getY())
                .toString();
    }

    public BigInteger getX(){
        return getVal("x");
    }

    public BigInteger getY(){
        return getVal("y");
    }

    private BigInteger getVal(String nom){
        Assert.notNull(nom,"Nom ne doit pas être null");
        BigInteger res=BigInteger.ZERO;
        for(var x:listeVariables){
            if(x.getNom().startsWith(nom)){
                res=res.add(BigInteger.valueOf(x.getValeur()).multiply(BigInteger.TEN.pow(x.getNo())));
            }
        }
        return res;
    }
}
