package org.jfactory.jfactory.domain;

import com.google.common.base.Preconditions;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Equation {

    private final List<Addition> additions;
    private final String valeur;
    private final List<Constante> valeurs;

    private final BigInteger valeurBi;

    private final List<Variable> listeVariables;

    private Duration dureeEstValide=Duration.ZERO;

    private long nbEstValide=0;

    public Equation(List<Addition> additions, String valeur, List<Constante> valeurs) {
        this.additions = List.copyOf(additions);
        this.valeur = valeur;
        this.valeurs = List.copyOf(valeurs);
        valeurBi = new BigInteger(valeur);
        listeVariables = getListVariables(additions);
    }

    private List<Variable> getListVariables(List<Addition> additions) {
        List<Variable> liste = new ArrayList<>();
        for (var add : additions) {
            for (var v : add.getToutesVariables()) {
                if (!liste.contains(v)) {
                    liste.add(v);
                }
            }
        }
        return List.copyOf(liste);
    }

    public Addition getEquationSimplifiee(int ordre) {
        Preconditions.checkNotNull(additions);
//        Preconditions.checkArgument(ordre<additions.size(),"ordre=%d,size=%d",ordre,additions.size());
        List<Operation> liste = new ArrayList<>();
        var n = 0;
        if (ordre < additions.size()) {
            var add = additions.get(ordre);
            for (var i = 0; i < add.getAddition().size(); i++) {
                var op = add.getAddition().get(i);
                if (op instanceof Multiplication mult) {
                    if (mult.getV1().isAffecte() && mult.getV2().isAffecte()) {
                        n += mult.getV1().getValeur() * mult.getV2().getValeur();
                    } else {
                        liste.add(mult);
                    }
                } else if (op instanceof Constante cst) {
                    n += cst.getValeur();
                } else {
                    Assert.state(false, "operation non gere: " + op);
                }
            }
        }
        if (n != 0) {
            liste.add(new Constante(n));
        }
        return new Addition(liste, ordre, valeurs.get(ordre).getValeur());
    }

    public BigInteger calcul(int ordre) {
        var debut= Instant.now();
        var res = BigInteger.ZERO;

        for (var i = 0; ((ordre > -1 && i <= ordre) || (ordre == -1) && i < additions.size()); i++) {
            var add = additions.get(i);
            for (var m : add.getAddition()) {
                if (m instanceof Multiplication mult) {
                    if (mult.getV1().isAffecte() && mult.getV2().isAffecte()) {
                        var v1 = BigInteger.valueOf(mult.getV1().getValeur());
                        var v2 = BigInteger.valueOf(mult.getV2().getValeur());
                        var bi = v1.multiply(v2);
                        res = res.add(bi.multiply(BigInteger.TEN.pow(i)));
                    } else {
                        return BigInteger.valueOf(-1);
                    }
                } else if (m instanceof Constante cst) {
                    res = res.add(BigInteger.valueOf(cst.getValeur()));
                } else {
                    Assert.state(false, "operation non gere: " + m);
                }
            }
        }

        var duree= Duration.between(debut,Instant.now());
        dureeEstValide=dureeEstValide.plus(duree);
        nbEstValide++;

        return res;
    }

    public boolean estValide(int ordre, boolean exacte) {
        BigInteger res;
        if (exacte) {
            res = calcul(-1);
        } else {
            res = calcul(ordre);
        }
        if (res.equals(BigInteger.valueOf(-1))) {
            return false;
        }
        if (exacte) {
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

    public int getMax() {
        return valeurs.size();
    }

    public Resultat getResolution() {
        List<Variable> liste = new ArrayList<>();
        for (var add : additions) {
            for (var v : add.getToutesVariables()) {
                if (!liste.contains(v)) {
                    var v2 = new Variable(v);
                    liste.add(v2);
                }
            }
        }
        return new Resultat(List.copyOf(liste), valeurs, valeurBi);
    }

    public Optional<Variable> getVariable(boolean x, int no){
        return listeVariables.stream()
                .filter(v-> v.isX()==x&&v.getNo()==no)
                .findFirst();
    }

    public long getNbVariable(boolean x){
        return listeVariables.stream()
                .filter(v-> v.isX()==x)
                .count();
    }

    public Optional<EquationSimple> calculEquationSimplifie(int ordre){

        var reste=calcul(ordre-1);
        reste=reste.max(BigInteger.ONE);
        var rest2=0L;
        if(ordre>0) {
            rest2=reste.divide(BigInteger.TEN.pow(ordre - 1)).longValueExact();
        }
        var tmp2=BigInteger.ZERO;

        Optional<Integer> a=Optional.empty();
        Optional<Integer> b=Optional.empty();

        var add = additions.get(ordre);
        for (var m : add.getAddition()) {
            if (m instanceof Multiplication mult) {
                if (mult.getV1().isAffecte() && mult.getV2().isAffecte()) {
                    var v1 = BigInteger.valueOf(mult.getV1().getValeur());
                    var v2 = BigInteger.valueOf(mult.getV2().getValeur());
                    var bi = v1.multiply(v2);
                    tmp2 = tmp2.add(bi);
                } else {
                    if(mult.getV1().isAffecte()&&!mult.getV2().isAffecte()){
                        if(mult.getV2().isX()){
                            Preconditions.checkState(a.isEmpty());
                            a=Optional.of(mult.getV1().getValeur());
                        } else {
                            Preconditions.checkState(b.isEmpty());
                            b=Optional.of(mult.getV1().getValeur());
                        }
                    } else if(!mult.getV1().isAffecte()&&mult.getV2().isAffecte()){
                        if(mult.getV1().isX()){
                            Preconditions.checkState(a.isEmpty());
                            a=Optional.of(mult.getV2().getValeur());
                        } else {
                            Preconditions.checkState(b.isEmpty());
                            b=Optional.of(mult.getV2().getValeur());
                        }
                    }
                }
            } else if (m instanceof Constante cst) {
                tmp2 = tmp2.add(BigInteger.valueOf(cst.getValeur()));
            } else {
                Assert.state(false, "operation non gere: " + m);
            }
        }

        EquationSimple equationSimple = new EquationSimple(a,b,Optional.of(tmp2.intValueExact()),valeurs.get(ordre).getValeur(), reste,rest2);
        return Optional.of(equationSimple);
    }

    public String toStringSimplifie(int ordre){
        if(ordre>=0){
            return  additions.stream()
                    .filter(x->x.getOrdre()<=ordre)
                    .map(Addition::toString2)
                    .collect(Collectors.joining(","));
        } else {
            return  additions.stream()
                    .map(Addition::toString2)
                    .collect(Collectors.joining(","));
        }
    }

    public String getDuration(){
        return ""+(dureeEstValide.getSeconds()/nbEstValide)+"s (nb="+nbEstValide+")";
    }

}
