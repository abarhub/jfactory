package org.jfactory.jfactory.valeurspossibles;

import org.jfactory.jfactory.domain.Equation;
import org.jfactory.jfactory.domain.Variable;

import java.util.List;

public interface ListeValeursPossibles {

    List<List<Integer>> getListeValeurPossibles(Equation equation, int ordre, List<Variable> listeVariables);

}
