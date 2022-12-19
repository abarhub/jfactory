package org.jfactory.jfactory.listener;

import org.jfactory.jfactory.domain.Doublet;
import org.jfactory.jfactory.domain.Resultat;
import org.jfactory.jfactory.domain.Variable;

import java.util.List;

public interface ParcourtListener {


    void trouve(int ordre, Resultat resultat);

    void entre(int ordre);

    void sort(int ordre);

    void variablesAAffecter(int ordre, List<Variable> listeVariables);

    void affecte(int ordre, List<Variable> listeVariables);

    void desaffecte(int ordre, List<Variable> listeVariables);

    void debut();

    void fin();

    void invalide(int ordre);

    void valeurPossibles(int ordre, List<Variable> listeVariables, List<Doublet> listeValeursPossibles);
}
