package org.jfactory.jfactory.domain;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListeValeursPossiblesSimple implements ListeValeursPossibles{

    private Map<Integer, List<List<Integer>>> map = new HashMap<>();

    public List<List<Integer>> getListeValeurPossibles(Equation equation, int ordre, List<Variable> listeVariables) {

        Preconditions.checkNotNull(equation);
        Preconditions.checkState(ordre>=0);
        Preconditions.checkNotNull(listeVariables);
        final var nbVariables=listeVariables.size();
        Assert.state(nbVariables == 1 || nbVariables == 2, () -> "Nombre de variable invalide: " + nbVariables);

        if (map.containsKey(nbVariables)) {
            return map.get(nbVariables);
        } else if (nbVariables == 2) {
            List<List<Integer>> listeValeursPossibles = new ArrayList<>();
            for (var i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    List<Integer> liste = new ArrayList<>();
                    liste.add(i);
                    liste.add(j);
                    listeValeursPossibles.add(ImmutableList.copyOf(liste));
                }
            }
            map.put(nbVariables, ImmutableList.copyOf(listeValeursPossibles));
            return map.get(nbVariables);
        } else if (nbVariables == 1) {
            List<List<Integer>> listeValeursPossibles = new ArrayList<>();
            for (var i = 0; i < 10; i++) {
                listeValeursPossibles.add(ImmutableList.of(i));
            }
            map.put(nbVariables, ImmutableList.copyOf(listeValeursPossibles));
            return map.get(nbVariables);
        } else {
            Assert.state(false, "nombre de variable invalide");
        }

        return null;
    }

}
