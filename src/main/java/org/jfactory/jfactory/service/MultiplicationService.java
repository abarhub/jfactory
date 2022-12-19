package org.jfactory.jfactory.service;

import org.jfactory.jfactory.domain.*;
import org.jfactory.jfactory.listener.ParcourtListener;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiplicationService.class);

    private ListeValeursPossibles listeValeursPossibles=new ListeValeursPossiblesSimple();

    private List<ParcourtListener> parcourtListeners=new ArrayList<>();

    public Equation generationEquation(String nombre) {

        Assert.notNull(nombre, "Le parametre nombre ne doit pas être null " + nombre);

        List<Addition> additions = new ArrayList<>();
        List<Constante> nombres = new ArrayList<>();
        Map<String, Variable> map = new HashMap<>();

        final var maxY = (int) Math.ceil(nombre.length() / 2.0);

        for (var i = 0; i < nombre.length(); i++) {
            var nom = "x" + i;
            var v = new Variable(nom, i);
            map.put(nom, v);
        }

        for (var i = 0; i < maxY; i++) {
            var nom = "y" + i;
            var v = new Variable(nom, i);
            map.put(nom, v);
        }

        var ordreMax = nombre.length() * 2;

        for (var ordre = 0; ordre < ordreMax; ordre++) {
            for (int i = 0; i < nombre.length(); i++) {

                for (int j = 0; j < maxY; j++) {
                    if (i + j == ordre) {
                        var x = "x" + i;
                        var y = "y" + j;
                        Variable v1, v2;
                        if (map.containsKey(x) && map.containsKey(y)) {
                            v1 = map.get(x);
                            Assert.notNull(v1, () -> "variable " + x + " inexistant(" + map + ")");
                            v2 = map.get(y);
                            Assert.notNull(v2, () -> "variable " + y + " inexistant(" + map + ")");

                            var ordre0 = ordre;
                            var m = additions.stream().filter(x2 -> x2.getOrdre() == ordre0).findFirst();
                            if (m.isPresent()) {
                                m.get().addOperation(new Multiplication(v1, v2));
                            } else {
                                int n = getNumber(nombre, ordre);
                                Assert.state(n >= 0, () -> "nombre invalide:" + n);
                                Assert.state(n <= 9, () -> "nombre invalide:" + n);
                                var liste2 = new ArrayList<Operation>();
                                liste2.add(new Multiplication(v1, v2));
                                var add = new Addition(liste2, ordre, n);
                                additions.add(add);
                                nombres.add(new Constante(n));
                            }

                        }
                    }
                }
            }
        }

        return new Equation(additions, nombre, nombres);
    }

    private static int getNumber(String nombre, int ordre) {
        int n;
        if (ordre < nombre.length()) {
            char c = nombre.charAt(nombre.length() - ordre - 1);
            n = c - '0';
        } else {
            n = 0;
        }
        return n;
    }

    public List<Resultat> resolution(Equation equation) {
        for(var parcourt:parcourtListeners){
            parcourt.debut();
        }
        var res= resolution(equation, 0);
        for(var parcourt:parcourtListeners){
            parcourt.fin();
        }
        return res;
    }

    private List<Resultat> resolution(Equation equation, int ordre) {

        List<Resultat> listeResultat = new ArrayList<>();

        var add = equation.getEquationSimplifiee(ordre);
        LOGGER.debug("ordre {} : {}", ordre, add);

        var listeVariables = add.getVariablesNonAffectees();

        Assert.notNull(listeVariables, "listeVariables est null (ordre=" + ordre + ")");

        for(var parcourt:parcourtListeners){
            parcourt.variablesAAffecter(ordre,listeVariables);
        }

        if (listeVariables.size() > 0) {

            List<List<Integer>> listeValeursPossibles = this.listeValeursPossibles.getListeValeurPossibles(equation,ordre,listeVariables);

            if (listeValeursPossibles != null) {
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
                    for(var parcourt:parcourtListeners){
                        parcourt.affecte(ordre,listeVariables);
                    }

                    // ajout du résultat si la valeur est bonne
                    ajouteResultat(equation, ordre, listeResultat);

                    // enleve l'affectation
                    for(var parcourt:parcourtListeners){
                        parcourt.desaffecte(ordre,listeVariables);
                    }
                    for (var i = 0; i < tmp.size() && i < listeVariables.size(); i++) {
                        listeVariables.get(i).setAffecte(false);
                        listeVariables.get(i).setValeur(-1);
                    }
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
                for(var parcourt:parcourtListeners){
                    parcourt.entre(ordre+1);
                }
                var res = resolution(equation, ordre + 1);
                for(var parcourt:parcourtListeners){
                    parcourt.sort(ordre+1);
                }
                listeResultat.addAll(res);
            } else {
                var res = equation.getResolution();
                LOGGER.atInfo().log("Trouve: {}", res);
                for(var parcourt:parcourtListeners){
                    parcourt.trouve(ordre,res);
                }
                listeResultat.add(res);
            }
        }
    }

    public ListeValeursPossibles getListeValeursPossibles() {
        return listeValeursPossibles;
    }

    public void setListeValeursPossibles(ListeValeursPossibles listeValeursPossibles) {
        this.listeValeursPossibles = listeValeursPossibles;
    }

    public void ajouteListener(ParcourtListener parcourtListener){
        this.parcourtListeners.add(parcourtListener);
    }

    public void supprimerListener(ParcourtListener parcourtListener){
        this.parcourtListeners.remove(parcourtListener);
    }
}
