package org.jfactory.jfactory.service;

import com.google.common.collect.ImmutableList;
import org.jfactory.jfactory.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MultiplicationService {

    private static Logger LOGGER = LoggerFactory.getLogger(MultiplicationService.class);

    private Map<Integer,List<List<Integer>>> map=new HashMap<>();

    public Equation generationEquation(String nombre) {

        Assert.notNull(nombre, "Le parametre nombre ne doit pas être null " + nombre);

        List<Addition> additions = new ArrayList<>();
        List<Constante> nombres = new ArrayList<>();
        Map<String, Variable> map = new HashMap<>();

        final var maxY = (int) Math.ceil(nombre.length() / 2.0);

        for(var i=0;i<nombre.length();i++){
            var nom="x"+i;
            var v = new Variable(nom, i);
            map.put(nom, v);
        }

        for(var i=0;i<maxY;i++){
            var nom="y"+i;
            var v = new Variable(nom, i);
            map.put(nom, v);
        }

        var ordreMax=nombre.length()*2;

        for(var ordre=0;ordre<ordreMax;ordre++) {
            for (int i = 0; i < nombre.length(); i++) {
//            char c = nombre.charAt(nombre.length() - i - 1);

//            int n = c - '0';
                int n;
//                List<Operation> liste = new ArrayList<>();


                //for (int j = 0; j <= i; j++) {
                //var j2 = (i - j);
                for (int j = 0; j < maxY; j++) {
//                    var j2 = j;
                    if(i+j==ordre) {
//                if (j2 < maxY||true) {
                        var x = "x" + i;
                        var y = "y" + j;
                        Variable v1, v2;
                        if (map.containsKey(x) && map.containsKey(y)) {
                            v1 = map.get(x);
                            Assert.notNull(v1, () -> "variable " + x + " inexistant(" + map + ")");
                            v2 = map.get(y);
                            Assert.notNull(v2, () -> "variable " + y + " inexistant(" + map + ")");
//                    v1 = getVar(map, x, j);
//                    v2 = getVar(map, y, j2);
//                        for(var m:additions){
//                            m.getOrdre()
//                        }
//                            var ordre = i + j2;
                            if (ordre < nombre.length()) {
                                //char c = nombre.charAt(nombre.length() - i - 1);
                                char c = nombre.charAt(nombre.length() - ordre - 1);
                                n = c - '0';
                            } else {
                                n = 0;
                            }
                            var n0=n;
                            Assert.state(n>=0,()->"nombre invalide:"+n0);
                            Assert.state(n<=9,()->"nombre invalide:"+n0);
                            var ordre0=ordre;
                            var m = additions.stream().filter(x2 -> x2.getOrdre() == ordre0).findFirst();
                            if (m.isPresent()) {
                                m.get().addOperation(new Multiplication(v1, v2));
                            } else {
                                var liste2 = new ArrayList<Operation>();
                                liste2.add(new Multiplication(v1, v2));
                                var add = new Addition(liste2, ordre, n);
                                additions.add(add);
//                            additions.add(new Multiplication(v1, v2));
                                nombres.add(new Constante(n));
                            }
//                        liste.add(new Multiplication(v1, v2));

                        }
                    }
//                }
                }

//            var add = new Addition(liste, i, n);
//            additions.add(add);

//            nombres.add(new Constante(n));
            }
        }

        return new Equation(additions, nombre, nombres);
    }

    private Variable getVar(Map<String, Variable> map, String nom, int no) {
        Variable v1;
        if (map.containsKey(nom)) {
            v1 = map.get(nom);
        } else {
            v1 = new Variable(nom, no);
            map.put(nom, v1);
        }
        return v1;
    }

    public void resolution(Equation equation) {
        resolution(equation, 0);
    }

    private List<Resultat> resolution(Equation equation, int ordre) {

        List<Resultat> listeResultat = new ArrayList<>();

        var add = equation.getEquationSimplifiee(ordre);
        LOGGER.debug("ordre {} : {}", ordre, add);

        var listeVariables = add.getVariablesNonAffectees();

        Assert.notNull(listeVariables, "listeVariables est null (ordre=" + ordre + ")");

        if(listeVariables.size()>0) {

            List<List<Integer>> listeValeursPossibles = getListeValeurPossibles(listeVariables.size());

            for (var tmp : listeValeursPossibles) {

                Assert.isTrue(tmp.size() <= 2, "listeVariables est superieur à 2 " +
                        "(size=" + tmp.size() + ",ordre=" + ordre + ",var=" + listeVariables + ")");
                Assert.isTrue(listeVariables.size() <= tmp.size(), "listeVariables est superieur à 2 " +
                        "(size=" + listeVariables.size() + ",ordre=" + ordre + ")");

                // affectation des variables
                for (var i = 0; i < tmp.size() && i < listeVariables.size(); i++) {
                    var valeur = tmp.get(i);
                    listeVariables.get(i).setAffecte(true);
                    listeVariables.get(i).setValeur(valeur);
                }

                ajouteResultat(equation, ordre, listeResultat);

                for (var i = 0; i < tmp.size() && i < listeVariables.size(); i++) {
                    listeVariables.get(i).setAffecte(false);
                    listeVariables.get(i).setValeur(-1);
                }

            }
        } else {

            ajouteResultat(equation, ordre, listeResultat);
        }


        return listeResultat;
    }

    private void ajouteResultat(Equation equation, int ordre, List<Resultat> listeResultat) {
        if (equation.estValide(ordre, ordre + 1 >= equation.getMax())) {
            if (ordre + 1 < equation.getMax()) {
                var res = resolution(equation, ordre + 1);
                listeResultat.addAll(res);
            } else {
                var res = equation.getResolution();
                LOGGER.atInfo().log("Trouve: {}", res);
                listeResultat.add(res);
            }
        }
    }


    private List<List<Integer>> getListeValeurPossibles(int nbVariables) {

        Assert.state(nbVariables==1||nbVariables==2,()->"Nombre de variable invalide: "+nbVariables);

        if(map.containsKey(nbVariables)){
            return map.get(nbVariables);
        } else if(nbVariables==2){
            List<List<Integer>> listeValeursPossibles = new ArrayList<>();
            for (var i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    List<Integer> liste = new ArrayList<>();
                    liste.add(i);
                    liste.add(j);
                    listeValeursPossibles.add(ImmutableList.copyOf(liste));
                }
            }
            map.put(nbVariables,ImmutableList.copyOf(listeValeursPossibles));
            return map.get(nbVariables);
        } else if(nbVariables==1){
            List<List<Integer>> listeValeursPossibles = new ArrayList<>();
            for (var i = 0; i < 10; i++) {
                listeValeursPossibles.add(ImmutableList.of(i));
            }
            map.put(nbVariables,ImmutableList.copyOf(listeValeursPossibles));
            return map.get(nbVariables);
        } else {
            Assert.state(false,"nombre de variable invalide");
        }

        return null;
    }

}
