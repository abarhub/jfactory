package org.jfactory.jfactory.listener;

import org.jfactory.jfactory.domain.Equation;
import org.jfactory.jfactory.domain.Resultat;
import org.jfactory.jfactory.domain.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ParcourtLog implements ParcourtListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParcourtLog.class);

    private final Equation equation;

    private Instant debut;

    private int nbTrouve;

    private int nbInvalide;

    private Map<Integer, BigInteger> mapInvalide = new TreeMap<>();

    public ParcourtLog(Equation equation) {
        this.equation = equation;
    }

    @Override
    public void trouve(int ordre, Resultat resultat) {
        LOGGER.atInfo().addKeyValue("ordre", ordre).log("trouver: ordre={}, resultat={}", ordre, resultat);
        nbTrouve++;
    }

    @Override
    public void entre(int ordre) {
        LOGGER.atDebug().addKeyValue("ordre", ordre).log("Entre ordre {}", ordre);
    }

    @Override
    public void sort(int ordre) {
        LOGGER.atDebug().addKeyValue("ordre", ordre).log("Sort ordre {}", ordre);
    }

    @Override
    public void variablesAAffecter(int ordre, List<Variable> listeVariables) {
        LOGGER.atDebug().addKeyValue("ordre", ordre).setMessage("Variables à affecter pour l'ordre {} : {}").addArgument(ordre).addArgument(() -> getVariables(listeVariables)).log();
    }

    @Override
    public void affecte(int ordre, List<Variable> listeVariables) {
        LOGGER.atDebug().addKeyValue("ordre", ordre).setMessage("Affectation pour l'ordre {} : {}").addArgument(ordre).addArgument(() -> getVariables(listeVariables)).log();
    }

    @Override
    public void desaffecte(int ordre, List<Variable> listeVariables) {
        LOGGER.atDebug().addKeyValue("ordre", ordre).setMessage("Désaffectation pour l'ordre {} : {}").addArgument(ordre).addArgument(() -> getVariables(listeVariables)).log();
    }

    @Override
    public void debut() {
        LOGGER.atInfo().log("Debut de parcourt. Equation={}", equation);
        debut = Instant.now();
    }

    @Override
    public void fin() {
        LOGGER.atInfo().setMessage("Fin de parcourt (duree={},nbTrouve={},nbInvalide={},mapInvalide={})")
                .addArgument(Duration.between(debut, Instant.now()))
                .addArgument(nbTrouve)
                .addArgument(nbInvalide)
                .addArgument(mapInvalide)
                .log();
    }

    private String getVariables(List<Variable> listeVariables) {
        if (listeVariables == null) {
            return "null";
        } else {
            return listeVariables.stream().map(Variable::toString2).collect(Collectors.joining(","));
        }
    }

    @Override
    public void invalide(int ordre) {
        nbInvalide++;
        if (!mapInvalide.containsKey(ordre)) {
            mapInvalide.put(ordre, BigInteger.ZERO);
        }
        mapInvalide.put(ordre, mapInvalide.get(ordre).add(BigInteger.ONE));
    }
}
