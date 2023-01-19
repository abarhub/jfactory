package org.jfactory.jfactory.listener;

import org.apache.commons.lang3.StringUtils;
import org.jfactory.jfactory.domain.Doublet;
import org.jfactory.jfactory.domain.Equation;
import org.jfactory.jfactory.domain.Resultat;
import org.jfactory.jfactory.domain.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class EnregistreParcourtListener implements ParcourtListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnregistreParcourtListener.class);

    public static final String COLONNE_SOLUTION="solution";
    public static final String COLONNE_ETAT="etat";
    public static final String COLONNE_CHEMIN_SOLUTION="chemin_solution";

    public static final String COLONNE_NO="no";
    public static final String COLONNE_ORDRE="ordre";
    public static final String COLONNE_NO_PARENT="no_parent";
    public static final String COLONNE_EQUATION="equation";

    private Map<Integer, Integer> mapX;

    private Map<Integer, Integer> mapY;

    private List<Map<String, String>> csv = new ArrayList<>();

    private final Equation equation;

    private long id;

    private Map<Integer,Long> dernierId=new HashMap<>();

    public EnregistreParcourtListener(Equation equation) {
        this.equation = equation;
        initialise();
    }

    private void initialise() {
        mapX = new TreeMap<>();
        id = 1L;
    }

    @Override
    public void trouve(int ordre, Resultat resultat) {
        var map = csv.get(csv.size() - 1);
        map.put(COLONNE_ETAT, "1");
        map.put(COLONNE_SOLUTION, "1");
        map.put(COLONNE_CHEMIN_SOLUTION, "1");
        int no=csv.size() - 2;
        int ordre2=ordre-1;
        for(int i=no;i>0;i--){
            map=csv.get(i);
            if(map.containsKey(COLONNE_ORDRE)){
                var s=map.get(COLONNE_ORDRE);
                if(StringUtils.equals(s,ordre2+"")){
                    map.put(COLONNE_CHEMIN_SOLUTION,"1");
                    ordre2=ordre2-1;
                }
            }
        }
    }

    @Override
    public void entre(int ordre) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(COLONNE_ORDRE, ordre + "");
        for (int i = 0; i < equation.getNbVariable(true); i++) {
            var v = equation.getVariable(true, i).get();
            if (v.isAffecte()) {
                map.put(v.getNom(), v.getValeur() + "");
            } else {
                map.put(v.getNom(), "");
            }
        }
        for (int i = 0; i < equation.getNbVariable(false); i++) {
            var v = equation.getVariable(false, i).get();
            if (v.isAffecte()) {
                map.put(v.getNom(), v.getValeur() + "");
            } else {
                map.put(v.getNom(), "");
            }
        }
        map.put(COLONNE_ETAT, "0");
        map.put(COLONNE_SOLUTION, "0");
        map.put(COLONNE_CHEMIN_SOLUTION, "0");
        map.put(COLONNE_NO, id + "");
        map.put(COLONNE_NO_PARENT, "");

        if(dernierId.containsKey(ordre-1)){
            map.put(COLONNE_NO_PARENT, dernierId.get(ordre-1) + "");
        }

        final var id0=id;

        dernierId.put(ordre,id0);

        id++;

        csv.add(map);

        //if(ordre<4) {
            var tmp2=equation.calculEquationSimplifie(ordre);
            if (tmp2.isPresent()) {
                var tmp3 = tmp2.get();

//                LOGGER.atInfo().log("no={},a={},b={},c={},n={},rest={}, ordre={},eq={}", id - 1,
//                        toString(tmp3.getA()), toString(tmp3.getB()), toString(tmp3.getC()), tmp3.getN(),tmp3.getRest(), ordre, equation);
                map.put("a", toString(tmp3.getA()));
                map.put("b", toString(tmp3.getB()));
                map.put("c", toString(tmp3.getB()));
                map.put("n", tmp3.getN()+"");
                map.put("rest", tmp3.getRest()+"");
                map.put("rest2", tmp3.getRest2()+"");
            }
//        }


        map.put(COLONNE_EQUATION,"\""+toString(equation, ordre)+"\"");


    }

    private String toString(Equation equation, int ordre) {
        return equation.toStringSimplifie(ordre);
    }

    private String toString(Optional<Integer> opt){
        if(opt.isPresent()){
            return opt.get()+"";
        } else {
            return "";
        }
    }

    @Override
    public void sort(int ordre) {

    }

    @Override
    public void variablesAAffecter(int ordre, List<Variable> listeVariables) {

    }

    @Override
    public void affecte(int ordre, List<Variable> listeVariables) {

    }

    @Override
    public void desaffecte(int ordre, List<Variable> listeVariables) {

    }

    @Override
    public void debut() {

    }

    @Override
    public void fin() {

    }

    @Override
    public void invalide(int ordre) {
        var map = csv.get(csv.size() - 1);
        map.put("etat", "2");
    }

    @Override
    public void valeurPossibles(int ordre, List<Variable> listeVariables, List<Doublet> listeValeursPossibles) {

    }

    public void writerFile(Path p) throws IOException {
        boolean premier = true;
        StringBuilder buf = new StringBuilder();
        for (var tmp : csv) {
            if (premier) {
                premier = false;
                var prem = true;
                for (var tmp2 : tmp.entrySet()) {
                    if (!prem) {
                        buf.append(",");
                    }
                    prem = false;
                    buf.append("").append(tmp2.getKey());
                }
                buf.append("\n");
            }
            var prem = true;
            for (var tmp2 : tmp.entrySet()) {
                if (!prem) {
                    buf.append(",");
                }
                prem = false;
                buf.append("").append(tmp2.getValue());
            }
            buf.append("\n");
        }
        Files.writeString(p, buf.toString());
    }
}
