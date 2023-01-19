package org.jfactory.jfactory.domain;

import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Addition {
    private List<Operation> addition;
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

    public void addOperation(Operation operation) {
        List<Operation> liste = new ArrayList<>(addition);
        liste.add(operation);
        this.addition = List.copyOf(liste);
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

    public String toString2() {
        return addition.stream().map(Operation::toString2)
                .collect(Collectors.joining("+")) +
                "=" + valeur + "(" + ordre + ")";
    }

    public List<Variable> getToutesVariables() {
        return getVariables(false);
    }

    public List<Variable> getVariablesNonAffectees() {
        return getVariables(true);
    }

    private List<Variable> getVariables(boolean nonAffecte) {
        var liste = new ArrayList<Variable>();
        for (var elt : addition) {
            if (elt instanceof Multiplication mult) {
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
                Assert.state(false, "Operation non geree : " + elt);
            }
        }
        return liste;
    }

//    public Optional<EquationSimple> getEquationSimplifie(Equation equation){
//        List<Multiplication> listeVariables = new ArrayList<>();
//        BigInteger rest;
//        {
//            var n = BigInteger.ZERO;
//            if (ordre > 0) {
//                n = equation.calcul(ordre - 1);
//                if (n.signum() < 0) {
//                    n = BigInteger.ZERO;
//                } else {
//                    n = n.divide(BigInteger.TEN.pow(ordre));
//                }
//            }
//            rest=n;
//        }
//        var res=equation.getValeurs().get(ordre);
//        long c = 0;
//        for (var op : getAddition()) {
//            if (op instanceof Multiplication m) {
//                if (m.getV1().isAffecte() && m.getV2().isAffecte()) {
//                    c += m.getV1().getValeur() * m.getV2().getValeur();
//                } else {
//                    listeVariables.add(m);
//                }
//            } else if (op instanceof Constante cst) {
//                c += cst.getValeur();
//            }
//        }
//        if (listeVariables.size() == 2) {
//            List<Integer> liste = new ArrayList<>();
//            long n = addConstantes(liste, getAddition().get(0));
//            n += addConstantes(liste, getAddition().get(1));
//            n += rest.longValueExact();
//            n += c;
//            Assert.state(liste.size() == 2, "Liste des constantes invalides");
//            Triplet t = new Triplet(liste.get(0), liste.get(1), normaliste(res.getValeur(), -n));
//            var v=getVar(getAddition().get(0));
//            int a,b,c0,n0;
//            if(v.isX()){
//                a=liste.get(0);
//                b=liste.get(1);
//            } else {
//                a=liste.get(1);
//                b=liste.get(0);
//            }
//            c0=res.getValeur();
//            n0=normaliste(0,n);
//            EquationSimple equationSimple = new EquationSimple(a,b,c0,n0, rest.longValueExact());
//            return Optional.of(equationSimple);
////            return t;
////            if (mapTriple.containsKey(t)) {
////                var tmp= Objects.requireNonNull(mapTriple.get(t));
////                if(!tmp.isEmpty()){
////                    var v=getVar(getAddition().get(0));
////                    if(StringUtils.startsWithIgnoreCase(v.getNom(),"y")) {
////                        tmp = tmp.stream().map(x -> new Doublet(x.getY(),x.getX())).toList();
////                    }
////                }
////                //return tmp;
////            } else {
////                //return List.of();
////            }
//        } else if (listeVariables.size() == 1) {
//            var v=getVar(listeVariables.get(0));
//            int a,b,c0,n0,c2;
//            var v2=listeVariables.get(0).getV1();
//            if(v2.isAffecte()){
//                c2=v2.getValeur();
//            } else {
//                c2=listeVariables.get(0).getV2().getValeur();
//            }
//            if(v.isX()){
//                a=c2;
//                b=0;
//            } else {
//                a=0;
//                b=c2;
//            }
//            List<Integer> liste = new ArrayList<>();
//            long n = addConstantes(liste, getAddition().get(0));
////            n += addConstantes(liste, getAddition().get(1));
//            n += rest.longValueExact();
//            n += c;
//            c0=res.getValeur();
//            n0=normaliste(0,n);
//            EquationSimple equationSimple = new EquationSimple(a,b,c0,n0, rest.longValueExact());
//            return Optional.of(equationSimple);
////            Assert.state(mapDoulet.containsKey(res.getValeur()), () -> "valeur non precalcule:" + res.getValeur() + ",mapDoulet=" + mapDoulet);
////            return Objects.requireNonNull(mapDoulet.get(res.getValeur()));
//        } else {
////            Assert.state(false, "Nombre d'addiction invalide:" + getAddition());
//            return Optional.empty();
//        }
//    }
//
//    private Variable getVar(Operation operation) {
//        Multiplication m= (Multiplication) operation;
//        if(!m.getV1().isAffecte()&&m.getV2().isAffecte()){
//            return m.getV1();
//        } else if(!m.getV2().isAffecte()){
//            return m.getV2();
//        } else {
//            Assert.state(false,"les variables sont toutes affectée:"+m);
//            return null;
//        }
//    }
//
//    protected int normaliste(int valeur, long n) {
//        long res = valeur + n;
//        if (res >= 0 && res < 10) {
//            return (int) res;
//        } else {
//            while (!(res >= 0 && res < 10)) {
//                if (res < 0) {
//                    res = res + 10;
//                } else {
//                    res = res - 10;
//                }
//            }
//            return (int) (res % 10);
//        }
//    }
//
//    private int addConstantes(List<Integer> liste, Operation operation) {
//        if (operation instanceof Multiplication m) {
//            if (m.getV1().isAffecte() && m.getV2().isAffecte()) {
//                return m.getV1().getValeur() + m.getV2().getValeur();
//            } else if (m.getV1().isAffecte() && !m.getV2().isAffecte()) {
//                liste.add(m.getV1().getValeur());
//            } else {
//                liste.add(m.getV2().getValeur());
//            }
//        } else if (operation instanceof Constante c) {
//            return c.getValeur();
//        } else {
//            Assert.state(false, "Operation invalide:" + operation);
//        }
//        return 0;
//    }

}
