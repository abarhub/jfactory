package org.jfactory.jfactory.domain;

import java.util.List;

public interface ListeValeursPossibles {

    List<List<Integer>> getListeValeurPossibles(Equation equation, int ordre, List<Variable> listeVariables);

}
