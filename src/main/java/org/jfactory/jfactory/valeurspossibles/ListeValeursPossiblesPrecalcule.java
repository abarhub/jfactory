package org.jfactory.jfactory.valeurspossibles;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.jfactory.jfactory.domain.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.*;

public class ListeValeursPossiblesPrecalcule implements ListeValeursPossibles {
    private Map<Triplet, List<Doublet>> mapTriple;

    private Map<Integer, List<Doublet>> mapDoulet;

    private List<Doublet> liste;

    @Override
    public List<Doublet> getListeValeurPossibles(Equation equation, int ordre, List<Variable> listeVariables, Addition add) {
        Preconditions.checkNotNull(equation);
        Preconditions.checkState(ordre >= 0);
        Preconditions.checkNotNull(listeVariables);
        Preconditions.checkNotNull(add);
        final var nbVariables = listeVariables.size();
        Assert.state(nbVariables == 1 || nbVariables == 2, () -> "Nombre de variable invalide: " + nbVariables);

        if (nbVariables == 2) {
            if (mapTriple == null) {
                precalcul();
            }
            var n = BigInteger.ZERO;
            if (ordre > 0) {
                n = equation.calcul(ordre - 1);
                if (n.signum() < 0) {
                    n = BigInteger.ZERO;
                } else {
                    n = n.divide(BigInteger.TEN.pow(ordre));
                }
            }
            return getValeurs(add, equation.getValeurs().get(ordre), n);
        } else if (nbVariables == 1) {
            return liste;
        } else {
            Assert.state(false, "nombre de variable invalide");
        }

        return null;
    }

    protected List<Doublet> getValeurs(Addition add, Constante res, BigInteger rest) {
        List<Multiplication> listeVariables = new ArrayList<>();
        long c = 0;
        for (var op : add.getAddition()) {
            if (op instanceof Multiplication m) {
                if (m.getV1().isAffecte() && m.getV2().isAffecte()) {
                    c += m.getV1().getValeur() * m.getV2().getValeur();
                } else {
                    listeVariables.add(m);
                }
            } else if (op instanceof Constante cst) {
                c += cst.getValeur();
            }
        }
        if (listeVariables.size() == 2) {
            List<Integer> liste = new ArrayList<>();
            long n = addConstantes(liste, add.getAddition().get(0));
            n += addConstantes(liste, add.getAddition().get(1));
            n += rest.longValueExact();
            n += c;
            Assert.state(liste.size() == 2, "Liste des constantes invalides");
            Triplet t = new Triplet(liste.get(0), liste.get(1), normaliste(res.getValeur(), -n));
            if (mapTriple.containsKey(t)) {
                var tmp=Objects.requireNonNull(mapTriple.get(t));
                if(!tmp.isEmpty()){
                    var v=getVar(add.getAddition().get(0));
                    if(StringUtils.startsWithIgnoreCase(v.getNom(),"y")) {
                        tmp = tmp.stream().map(x -> new Doublet(x.getY(),x.getX(), x.getId())).toList();
                    }
                }
                return tmp;
            } else {
                return List.of();
            }
        } else if (listeVariables.size() == 1) {
            Assert.state(mapDoulet.containsKey(res.getValeur()), () -> "valeur non precalcule:" + res.getValeur() + ",mapDoulet=" + mapDoulet);
            return Objects.requireNonNull(mapDoulet.get(res.getValeur()));
        } else {
            Assert.state(false, "Nombre d'addiction invalide:" + add.getAddition());
            return null;
        }
    }

    private Variable getVar(Operation operation) {
        Multiplication m= (Multiplication) operation;
        if(!m.getV1().isAffecte()&&m.getV2().isAffecte()){
            return m.getV1();
        } else if(!m.getV2().isAffecte()){
            return m.getV2();
        } else {
            Assert.state(false,"les variables sont toutes affectée:"+m);
            return null;
        }
    }

    protected int normaliste(int valeur, long n) {
        long res = valeur + n;
        if (res >= 0 && res < 10) {
            return (int) res;
        } else {
            while (!(res >= 0 && res < 10)) {
                if (res < 0) {
                    res = res + 10;
                } else {
                    res = res - 10;
                }
            }
            return (int) (res % 10);
        }
    }

    private static int addConstantes(List<Integer> liste, Operation operation) {
        if (operation instanceof Multiplication m) {
            if (m.getV1().isAffecte() && m.getV2().isAffecte()) {
                return m.getV1().getValeur() + m.getV2().getValeur();
            } else if (m.getV1().isAffecte() && !m.getV2().isAffecte()) {
                liste.add(m.getV1().getValeur());
            } else {
                liste.add(m.getV2().getValeur());
            }
        } else if (operation instanceof Constante c) {
            return c.getValeur();
        } else {
            Assert.state(false, "Operation invalide:" + operation);
        }
        return 0;
    }

    protected void precalcul() {
        long id=1;
        Map<Triplet, List<Doublet>> mapTriple = new HashMap<>();
        for (int a = 0; a < 10; a++) {
            for (int b = 0; b < 10; b++) {
                for (int x = 0; x < 10; x++) {
                    for (int y = 0; y < 10; y++) {
                        int c = (a * x + b * y) % 10;
                        Triplet t = new Triplet(a, b, c);
                        if (!mapTriple.containsKey(t)) {
                            mapTriple.put(t, new ArrayList<>());
                        }
                        mapTriple.get(t).add(new Doublet(x, y,id));
                        id++;
                    }
                }
            }
        }

        Map<Integer, List<Doublet>> mapDoulet = new HashMap<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                int c = (x * y) % 10;
                if (!mapDoulet.containsKey(c)) {
                    mapDoulet.put(c, new ArrayList<>());
                }
                mapDoulet.get(c).add(new Doublet(x, y, id));
                id++;
            }
        }

        List<Doublet> liste = new ArrayList<>();
        for (var i = 0; i < 10; i++) {
            liste.add(new Doublet(i, id));
            id++;
        }
        liste = ImmutableList.copyOf(liste);

        this.mapTriple = mapTriple;
        this.mapDoulet = mapDoulet;
        this.liste = liste;
    }

    protected Map<Triplet, List<Doublet>> getMapTriple() {
        return mapTriple;
    }

    protected Map<Integer, List<Doublet>> getMapDoulet() {
        return mapDoulet;
    }

    protected List<Doublet> getListe() {
        return liste;
    }
}
