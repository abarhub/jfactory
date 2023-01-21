package org.jfactory.jfactory.listener;

import org.apache.commons.lang3.StringUtils;
import org.jfactory.jfactory.domain.Doublet;
import org.jfactory.jfactory.domain.Equation;
import org.jfactory.jfactory.domain.Resultat;
import org.jfactory.jfactory.domain.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class EnregistreParcourtListener implements ParcourtListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnregistreParcourtListener.class);

    public static final String COLONNE_SOLUTION = "solution";
    public static final String COLONNE_ETAT = "etat";
    public static final String COLONNE_CHEMIN_SOLUTION = "chemin_solution";

    public static final String COLONNE_NO = "no";
    public static final String COLONNE_ORDRE = "ordre";
    public static final String COLONNE_NO_PARENT = "no_parent";
    public static final String COLONNE_EQUATION = "equation";

    public static final String COLONNE_A = "a";

    public static final String COLONNE_B = "b";

    public static final String COLONNE_C = "c";

    public static final String COLONNE_N = "n";

    public static final String COLONNE_REST = "rest";

    public static final String COLONNE_REST2 = "rest2";

    public static final String COLONNE_NOMBRE = "nombre";

    public static final String COLONNE_X = "x";


    public static final String COLONNE_Y = "y";

    public static final String COLONNE_AUTRE = "autre";

    public static final String COLONNE_ID_RESOLUTION = "ID_RESOLUTION";

    public static final String COLONNE_A2 = "a2";

    public static final String COLONNE_B2 = "b2";

    private Map<Integer, Integer> mapX;

    private Map<Integer, Integer> mapY;

    private List<Map<String, String>> csv = new ArrayList<>();

    private final Equation equation;

    private long id;

    private int ordre;

    private Map<Integer, Long> dernierId = new HashMap<>();

    private boolean enregistreEquation = false;

    private int logEquationOrdre = -1;

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
        var map = getLast();
        map.put(COLONNE_ETAT, "1");
        map.put(COLONNE_SOLUTION, "1");
        map.put(COLONNE_CHEMIN_SOLUTION, "1");
        int no = csv.size() - 2;
        int ordre2 = ordre - 1;
        for (int i = no; i > 0; i--) {
            map = csv.get(i);
            if (map.containsKey(COLONNE_ORDRE)) {
                var s = map.get(COLONNE_ORDRE);
                if (StringUtils.equals(s, ordre2 + "")) {
                    map.put(COLONNE_CHEMIN_SOLUTION, "1");
                    ordre2 = ordre2 - 1;
                }
            }
        }
    }

    private Map<String,String> getLast(){
        return csv.get(csv.size() - 1);
    }

    @Override
    public void entre(int ordre) {
        this.ordre = ordre;
    }

    private void enregistreLigne(int ordre, Doublet valeursSelectionnees, List<Variable> listeVariables) {
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

        if (dernierId.containsKey(ordre - 1)) {
            map.put(COLONNE_NO_PARENT, dernierId.get(ordre - 1) + "");
        }

        final var id0 = id;

        dernierId.put(ordre, id0);

        id++;

        csv.add(map);

        var tmp2 = equation.calculEquationSimplifie(ordre);
        if (tmp2.isPresent()) {
            var tmp3 = tmp2.get();

            if (logEquationOrdre >= 0 && ordre < logEquationOrdre) {
                LOGGER.atInfo().log("no={},a={},b={},c={},n={},rest={}, ordre={},eq={}", id - 1,
                        toString(tmp3.getA()), toString(tmp3.getB()), toString(tmp3.getC()), tmp3.getN(), tmp3.getRest(), ordre, equation);
            }
            map.put(COLONNE_A, toString(tmp3.getA()));
            map.put(COLONNE_B, toString(tmp3.getB()));
            map.put(COLONNE_C, toString(tmp3.getC()));
            map.put(COLONNE_N, tmp3.getN() + "");
            map.put(COLONNE_REST, tmp3.getRest() + "");
            map.put(COLONNE_REST2, tmp3.getRest2() + "");
        }


        if (enregistreEquation) {
            map.put(COLONNE_EQUATION, "\"" + toString(equation, ordre) + "\"");
        }

        map.put(COLONNE_NOMBRE, equation.getValeur());

        map.put(COLONNE_X, getVariable(true)+"");

        map.put(COLONNE_Y, getVariable(false)+"");

        map.put(COLONNE_ID_RESOLUTION, valeursSelectionnees.getId()+"");
        map.put(COLONNE_A2, (valeursSelectionnees.getX()>=0)?valeursSelectionnees.getX()+"":"");
        map.put(COLONNE_B2, (valeursSelectionnees.getY()>=0)?valeursSelectionnees.getY()+"":"");

    }

    private BigInteger getVariable(boolean x) {
        var res=BigInteger.ZERO;
        for(int i=0;i<equation.getNbVariable(x);i++){
            var v=equation.getVariable(x,i);
            if(v.isPresent()) {
                var v2=v.get();
                if(v2.isAffecte()) {
                    res = res.add(BigInteger.valueOf(v2.getValeur()).multiply(BigInteger.TEN.pow(i)));
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return res;
    }

    private String toString(Equation equation, int ordre) {
        return equation.toStringSimplifie(ordre);
    }

    private String toString(Optional<Integer> opt) {
        if (opt.isPresent()) {
            return opt.get() + "";
        } else {
            return "";
        }
    }

    @Override
    public void sort(int ordre) {
        this.ordre = ordre;
    }

    @Override
    public void variablesAAffecter(int ordre, List<Variable> listeVariables) {

    }

    @Override
    public void avantAffectation(int ordre, List<Variable> listeVariables, Doublet valeursSelectionnees) {
        enregistreLigne(ordre, valeursSelectionnees, listeVariables);
    }

    @Override
    public void affecte(int ordre, List<Variable> listeVariables, Doublet valeursSelectionnees) {

    }

    @Override
    public void desaffecte(int ordre, List<Variable> listeVariables) {

    }

    @Override
    public void debut() {
        ordre = 0;
    }

    @Override
    public void fin() {

    }

    @Override
    public void invalide(int ordre) {
        var map = getLast();
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
                    buf.append(tmp2.getKey());
                }
                buf.append("\n");
            }
            var prem = true;
            for (var tmp2 : tmp.entrySet()) {
                if (!prem) {
                    buf.append(",");
                }
                prem = false;
                buf.append(tmp2.getValue());
            }
            buf.append("\n");
        }
        Files.writeString(p, buf.toString());
    }
}
