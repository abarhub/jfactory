package org.jfactory.jfactory.valeurspossibles;

import org.jfactory.jfactory.domain.Addition;
import org.jfactory.jfactory.domain.Doublet;
import org.jfactory.jfactory.domain.Equation;
import org.jfactory.jfactory.domain.Variable;

import java.util.List;

public interface ListeValeursPossibles {

    List<Doublet> getListeValeurPossibles(Equation equation, int ordre, List<Variable> listeVariables, Addition add);

}
