package org.jfactory.jfactory.service;

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

    public Equation generationEquation(String nombre) {

        Assert.notNull(nombre, "Le parametre nombre ne doit pas être null " + nombre);

        List<Addition> additions = new ArrayList<>();
        List<Constante> nombres = new ArrayList<>();
        Map<String, Variable> map = new HashMap<>();

        for (int i = 0; i < nombre.length(); i++) {
            char c = nombre.charAt(nombre.length() - i - 1);

            int n = c - '0';
            List<Operation> liste = new ArrayList<>();

            for (int j = 0; j <= i; j++) {
                var x = "x" + j;
                var y = "y" + (i - j);
                Variable v1, v2;
                v1 = getVar(map, x, j);
                v2 = getVar(map, y, i - j);
                liste.add(new Multiplication(v1, v2));
            }

            var add = new Addition(liste, i, n);
            additions.add(add);

            nombres.add(new Constante(n));
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
        List<List<Integer>> listeValeursPossibles=getListeValeurPossibles();
        resolution(equation, 0, listeValeursPossibles);
    }

    private List<Resultat> resolution(Equation equation, int ordre,List<List<Integer>> listeValeursPossibles) {

        List<Resultat> listeResultat = new ArrayList<>();

        var add = equation.getEquationSimplifiee(ordre);
        LOGGER.debug("ordre {} : {}", ordre, add);

        var listeVariables = add.getVariablesNonAffectees();

        Assert.notNull(listeVariables, "listeVariables est null (ordre=" + ordre + ")");


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

            if (equation.estValide(ordre, ordre + 1 >= equation.getMax())) {
                if (ordre + 1 < equation.getMax()) {
                    var res = resolution(equation, ordre + 1, listeValeursPossibles);
                    listeResultat.addAll(res);
                } else {
                    var res = equation.getResolution();
                    LOGGER.atInfo().log("Trouve: {}", res);
                    listeResultat.add(res);
                }
            } else {

            }

            for (var i = 0; i < listeValeursPossibles.size() && i < listeVariables.size(); i++) {
                listeVariables.get(i).setAffecte(false);
                listeVariables.get(i).setValeur(-1);
            }

        }


        return listeResultat;
    }

    private List<List<Integer>> getListeValeurPossibles(){

        List<List<Integer>> listeValeursPossibles = new ArrayList<>();
        for (var i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                List<Integer> liste = new ArrayList<>();
                liste.add(i);
                liste.add(j);
                listeValeursPossibles.add(liste);
            }
        }
        return listeValeursPossibles;
    }

}
