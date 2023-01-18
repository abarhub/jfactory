package org.jfactory.jfactory.service;

import com.google.common.collect.Lists;
import org.jfactory.jfactory.domain.Addition;
import org.jfactory.jfactory.domain.Constante;
import org.jfactory.jfactory.domain.Equation;
import org.jfactory.jfactory.domain.Multiplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyseMultiplicationService {

    private static Logger LOGGER = LoggerFactory.getLogger(AnalyseMultiplicationService.class);

    record Mult(long a, long b, long n) {
    }

    record Param1(long a, long b, long c, long n) {
    }

    record Var(long x, long y) {
    }

    static interface Result {
        long nb();

        List<Equation> equationList();
    }

    static record ResultImpl(long nb, List<Equation> equationList) implements Result {
        @Override
        public String toString() {
            return new StringJoiner(", ", ResultImpl.class.getSimpleName() + "[", "]")
                    .add("nb=" + nb)
                    .toString();
        }
    }

    ;

    @FunctionalInterface
    interface Ajoute {

        void ajoute(long multX, long multY, int constante, long valx, long valy,
                    Addition tmp3, Equation equation, Map<Param1, Map<Var, Result>> map);
    }

    public void run() {
//        test1();
//        test2();
        test3();
    }

    public void test3() {
        LOGGER.atInfo().log("Analyse multiplication ...");

        int ordre, valeur;

        ordre = 2;
        valeur = 5;

        try {
            Path p = Path.of("files/double_primes_5.txt");
            List<Mult> liste = parseFileMultiplication(p, ordre, valeur);

            LOGGER.atInfo().log("liste={}", liste);
            LOGGER.atInfo().log("size(liste)={}", liste.size());

            Map<Param1, Map<Var, Result>> map = listeValeursPossibles2(liste, ordre, (long multX, long multY, int constante, long valx, long valy,
                                                                                      Addition tmp3, Equation equation, Map<Param1, Map<Var, Result>> map2) -> {

                var p2 = new Param1(multX, multY, constante % 10, tmp3.getValeur());
                var v2 = new Var(valx, valy);

                addMap(map2, p2, v2, equation);
            });

            LOGGER.atInfo().log("map={}", map);

            Map<Long, Long> map2 = new TreeMap<>();
            Map<Long, List<Equation>> map3 = new TreeMap<>();
            for (var tmp2 : map.entrySet()) {
                for (var tmp3 : tmp2.getValue().entrySet()) {
                    var n = tmp3.getValue().nb();
                    if (map2.containsKey(n)) {
                        map2.put(n, map2.get(n) + 1);
                    } else {
                        map2.put(n, 1L);
                    }
                    if(n>1){
                        if (map3.containsKey(n)) {
                            map3.get(n).addAll(tmp3.getValue().equationList());
                        } else {
                            map3.put(n, Lists.newArrayList(tmp3.getValue().equationList()));
                        }
                    }
                }
            }

            var iter=map3.entrySet().iterator();
            while(iter.hasNext()){
                var tmp5=iter.next();
                if(map2.get(tmp5.getKey())<4){
                    iter.remove();
                }
            }

            LOGGER.atInfo().log("map2={}", map2);

            LOGGER.atInfo().log("map3={}", map3);

            LOGGER.atInfo().log("map3 ={}", map3.entrySet().stream().filter(x->x.getValue().size()<4).collect(Collectors.toList()));

            for(var tmp:map3.entrySet()) {
                LOGGER.atInfo().log("map3({})={}", tmp.getKey(),tmp.getValue());
            }

        } catch (Exception e) {
            LOGGER.atError().log("Erreur", e);
        }

    }

    private Map<Param1, Map<Var, Result>> listeValeursPossibles2(List<Mult> liste, int ordre, Ajoute ajoute) {
        Map<Param1, Map<Var, Result>> map = new HashMap<>();

        for (int i = 0; i < liste.size(); i++) {
            var tmp = liste.get(i);

            MultiplicationService multiplicationService = new MultiplicationService();

            var eq = multiplicationService.generationEquation(tmp.n + "");

            LOGGER.atInfo().log("eq={}", eq);

            affectation(eq, true, tmp.a());
            affectation(eq, false, tmp.b);

            LOGGER.atInfo().log("eq={}", eq);

            var tmp2 = eq.getEquationSimplifiee(ordre);

            LOGGER.atInfo().log("eq ordre 3={}", tmp2);

            var x2 = eq.getVariable(true, ordre);
            var y2 = eq.getVariable(false, ordre);

            var valx2 = x2.get().getValeur();
            var valy2 = y2.get().getValeur();

            x2.get().setAffecte(false);
            x2.get().setValeur(-1);
            y2.get().setAffecte(false);
            y2.get().setValeur(-1);

            var tmp3 = eq.getEquationSimplifiee(ordre);
            LOGGER.atInfo().log("eq ordre no {} : {}", ordre, tmp3);

            long constante = 0;
            long multX = -1;
            long multY = -1;

            for (var tmp4 : tmp3.getAddition()) {
                if (tmp4 instanceof Constante cst) {
                    constante += cst.getValeur();
                } else if (tmp4 instanceof Multiplication m) {
                    var v1 = m.getV1();
                    var v2 = m.getV2();
                    if (v1.isAffecte()) {
                        Assert.state(!v2.isAffecte(), "v2 non affecté:" + tmp4);
                        if (v2.isX()) {
                            Assert.state(v2.getNom().equals(x2.get().getNom()), "v2 invalide");
                            multX = v1.getValeur();
                        } else {
                            Assert.state(v2.getNom().equals(y2.get().getNom()), "v2 invalide");
                            multY = v1.getValeur();
                        }
                    } else {
                        Assert.state(v2.isAffecte(), "v2 non affecté:" + tmp4);
                        if (v1.isX()) {
                            Assert.state(v1.getNom().equals(x2.get().getNom()), "v1 invalide");
                            multX = v2.getValeur();
                        } else {
                            Assert.state(v1.getNom().equals(y2.get().getNom()), "v1 invalide");
                            multY = v2.getValeur();
                        }
                    }
                } else {
                    Assert.state(false, "operation non géré:" + tmp4);
                }
            }
            Assert.state(multX >= 0, "mult x invalide");
            Assert.state(multY >= 0, "mult y invalide");

            LOGGER.atInfo().log("eq: {}*{} + {}*{} + {} = {}", multX, x2.get().getNom(), multY, y2.get().getNom(), constante, tmp3.getValeur());

//            var p2 = new Param1(multX, multY, constante%10, tmp3.getValeur());
//            var v2 = new Var(valx2, valy2);
//
//            addMap(map, p2, v2);
            ajoute.ajoute(multX, multY, valx2, valy2, constante, tmp3, eq, map);

        }

        return map;
    }

    public void test2() {
        LOGGER.atInfo().log("Analyse multiplication ...");

        int ordre, valeur;

        ordre = 2;
        valeur = 5;

        try {
            Path p = Path.of("files/double_primes_5.txt");
            List<Mult> liste = parseFileMultiplication(p, ordre, valeur);

            LOGGER.atInfo().log("liste={}", liste);
            LOGGER.atInfo().log("size(liste)={}", liste.size());


            Map<Param1, Map<Var, Result>> map = listeValeursPossibles(liste, ordre);

            LOGGER.atInfo().log("map={}", map);

            Map<Long, Long> map2 = new TreeMap<>();
            for (var tmp2 : map.entrySet()) {
                for (var tmp3 : tmp2.getValue().entrySet()) {
                    var n = tmp3.getValue().nb();
                    if (map2.containsKey(n)) {
                        map2.put(n, map2.get(n) + 1);
                    } else {
                        map2.put(n, 1L);
                    }
                }
            }

            LOGGER.atInfo().log("map2={}", map2);

        } catch (Exception e) {
            LOGGER.atError().log("Erreur", e);
        }

    }

    private Map<Param1, Map<Var, Result>> listeValeursPossibles(List<Mult> liste, int ordre) {
        return listeValeursPossibles2(liste, ordre, (long multX, long multY, int constante, long valx, long valy,
                                                     Addition tmp3, Equation equation, Map<Param1, Map<Var, Result>> map) -> {

            var p2 = new Param1(multX, multY, constante % 10, tmp3.getValeur());
            var v2 = new Var(valx, valy);

            addMap(map, p2, v2, null);
        });
    }

    private static void addMap(Map<Param1, Map<Var, Result>> map, Param1 p2, Var v2, Equation equation) {
        List<Equation> liste;
        if (map.containsKey(p2)) {
            var tmp4 = map.get(p2);
            if (tmp4.containsKey(v2)) {
                var tmp5 = tmp4.get(v2);
                liste = getEquations(equation, tmp5.equationList());
                tmp4.put(v2, new ResultImpl(tmp4.get(v2).nb() + 1, liste));
            } else {
                liste = getEquations(equation, null);
                tmp4.put(v2, new ResultImpl(1L, liste));
            }
        } else {
            var tmp4 = new HashMap<Var, Result>();
            liste = getEquations(equation, null);
            tmp4.put(v2, new ResultImpl(1L, liste));
            map.put(p2, tmp4);
        }
    }

    private static List<Equation> getEquations(Equation equation, List<Equation> listeInitiale) {
        List<Equation> liste;
        if (equation == null) {
            liste = listeInitiale;
        } else {
            if (CollectionUtils.isEmpty(listeInitiale)) {
                liste = Lists.newArrayList(equation);
            } else {
                liste = Lists.newArrayList(listeInitiale);
                liste.add(equation);
            }
        }
        return liste;
    }

    public void test1() {
        LOGGER.atInfo().log("Analyse multiplication ...");

        try {
            Path p = Path.of("files/double_primes_5.txt");
            List<Mult> liste = parseFileMultiplication(p, 2, 5);

            LOGGER.atInfo().log("liste={}", liste);
            LOGGER.atInfo().log("size(liste)={}", liste.size());

            var tmp = liste.get(0);

            MultiplicationService multiplicationService = new MultiplicationService();

            var eq = multiplicationService.generationEquation(tmp.n + "");

            LOGGER.atInfo().log("eq={}", eq);

            affectation(eq, true, tmp.a());
            affectation(eq, false, tmp.b);

            LOGGER.atInfo().log("eq={}", eq);

            var tmp2 = eq.getEquationSimplifiee(2);

            LOGGER.atInfo().log("eq ordre 3={}", tmp2);

            var x2 = eq.getVariable(true, 2);
            var y2 = eq.getVariable(false, 2);

            var valx2 = x2.get().getValeur();
            var valy2 = y2.get().getValeur();

            x2.get().setAffecte(false);
            x2.get().setValeur(-1);
            y2.get().setAffecte(false);
            y2.get().setValeur(-1);

            var tmp3 = eq.getEquationSimplifiee(2);
            LOGGER.atInfo().log("eq ordre no {} : {}", 2, tmp3);

            long constante = 0;
            long multX = -1;
            long multY = -1;

            for (var tmp4 : tmp3.getAddition()) {
                if (tmp4 instanceof Constante cst) {
                    constante += cst.getValeur();
                } else if (tmp4 instanceof Multiplication m) {
                    var v1 = m.getV1();
                    var v2 = m.getV2();
                    if (v1.isAffecte()) {
                        Assert.state(!v2.isAffecte(), "v2 non affecté:" + tmp4);
                        if (v2.isX()) {
                            Assert.state(v2.getNom().equals(x2.get().getNom()), "v2 invalide");
                            multX = v1.getValeur();
                        } else {
                            Assert.state(v2.getNom().equals(y2.get().getNom()), "v2 invalide");
                            multY = v1.getValeur();
                        }
                    } else {
                        Assert.state(v2.isAffecte(), "v2 non affecté:" + tmp4);
                        if (v1.isX()) {
                            Assert.state(v1.getNom().equals(x2.get().getNom()), "v1 invalide");
                            multX = v2.getValeur();
                        } else {
                            Assert.state(v1.getNom().equals(y2.get().getNom()), "v1 invalide");
                            multY = v2.getValeur();
                        }
                    }
                } else {
                    Assert.state(false, "operation non géré:" + tmp4);
                }
            }
            Assert.state(multX >= 0, "mult x invalide");
            Assert.state(multY >= 0, "mult y invalide");

            LOGGER.atInfo().log("eq: {}*{} + {}*{} + {} = {}", multX, x2.get().getNom(), multY, y2.get().getNom(), constante, tmp3.getValeur());

            var p2 = new Param1(multX, multY, constante, tmp3.getValeur());
            var v2 = new Var(valx2, valy2);

        } catch (Exception e) {
            LOGGER.atError().log("Erreur", e);
        }
    }

    private List<Mult> parseFileMultiplication(Path p, int ordre, int valeur) throws IOException {
        var liste = Files.lines(p)
                .map(x -> {
                    var tmp = x.split("=");
                    var n = tmp[1];
                    var tmp2 = tmp[0].split("\\*");
                    var a = tmp2[0];
                    var b = tmp2[1];
                    var n0 = Long.parseLong(n, 10);
                    var a0 = Long.parseLong(a, 10);
                    var b0 = Long.parseLong(b, 10);
                    return new Mult(a0, b0, n0);
                })
                .filter(x -> x.n >= 1000)
                .filter(x -> getValeurPos(x.n, ordre) == valeur)
                .collect(Collectors.toList());
        return liste;
    }

    private void affectation(Equation eq, boolean x, long valeur) {

        for (int i = 0; i < eq.getNbVariable(x); i++) {
            var v = eq.getVariable(x, i);
            Assert.isTrue(v.isPresent(), "Variable " + i + " absente");
            var v2 = v.get();
            var tmp2 = getValeurPos(valeur, i);
            LOGGER.atInfo().log("{}{}:{}({})", (x) ? "x" : "y", i, tmp2, v2);
            v2.setAffecte(true);
            v2.setValeur(tmp2);
        }
    }

    private int getValeurPos(long n, int pos) {
        long tmp = 1;
        for (int i = 0; i < pos; i++) {
            tmp = tmp * 10;
        }
        return (int) ((n / tmp) % 10);
    }
}
