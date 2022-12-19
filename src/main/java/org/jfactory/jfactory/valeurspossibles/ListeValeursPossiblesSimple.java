package org.jfactory.jfactory.valeurspossibles;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.jfactory.jfactory.domain.Addition;
import org.jfactory.jfactory.domain.Doublet;
import org.jfactory.jfactory.domain.Equation;
import org.jfactory.jfactory.domain.Variable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListeValeursPossiblesSimple implements ListeValeursPossibles {

    private Map<Integer, List<Doublet>> map = new HashMap<>();

    public List<Doublet> getListeValeurPossibles(Equation equation, int ordre, List<Variable> listeVariables, Addition add) {

        Preconditions.checkNotNull(equation);
        Preconditions.checkState(ordre >= 0);
        Preconditions.checkNotNull(listeVariables);
        Preconditions.checkNotNull(add);
        final var nbVariables = listeVariables.size();
        Assert.state(nbVariables == 1 || nbVariables == 2, () -> "Nombre de variable invalide: " + nbVariables);

        if (map.containsKey(nbVariables)) {
            return map.get(nbVariables);
        } else if (nbVariables == 2) {
            List<Doublet> listeValeursPossibles = new ArrayList<>();
            for (var i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    listeValeursPossibles.add(new Doublet(i, j));
                }
            }
            map.put(nbVariables, ImmutableList.copyOf(listeValeursPossibles));
            return map.get(nbVariables);
        } else if (nbVariables == 1) {
            List<Doublet> listeValeursPossibles = new ArrayList<>();
            for (var i = 0; i < 10; i++) {
                listeValeursPossibles.add(new Doublet(i));
            }
            map.put(nbVariables, ImmutableList.copyOf(listeValeursPossibles));
            return map.get(nbVariables);
        } else {
            Assert.state(false, "nombre de variable invalide");
        }

        return null;
    }

}
