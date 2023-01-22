package org.jfactory.jfactory.service;

import org.jfactory.jfactory.domain.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;

public class FermatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FermatService.class);

    private static final Set<BigInteger> TERMINAISON_CARRE = Set.of(BigInteger.valueOf(0),
            BigInteger.valueOf(1), BigInteger.valueOf(4), BigInteger.valueOf(9), BigInteger.valueOf(16),
            BigInteger.valueOf(21), BigInteger.valueOf(24), BigInteger.valueOf(25), BigInteger.valueOf(29),
            BigInteger.valueOf(36), BigInteger.valueOf(41), BigInteger.valueOf(44), BigInteger.valueOf(49),
            BigInteger.valueOf(56), BigInteger.valueOf(61), BigInteger.valueOf(64), BigInteger.valueOf(69),
            BigInteger.valueOf(76), BigInteger.valueOf(81), BigInteger.valueOf(84), BigInteger.valueOf(89),
            BigInteger.valueOf(96));

    private static final BigInteger BI_100 = BigInteger.valueOf(100);

    // La vérification est plus lente si la valeur est true
    private static final boolean OPTIMISE_VERIFICATION_CARRE = false;

    /**
     * Factorisation suivant la méthode de fermat
     * factorisation de fermat :
     * https://blogdemaths.wordpress.com/2014/05/18/savez-vous-factoriser-a-la-mode-de-fermat/
     * https://accromath.uqam.ca/2019/10/lheritage-de-fermat-pour-la-factorisation-des-grands-nombres/
     *
     * @param n Le nombre a factoriser Il doit être le produite de 2 nombres premiers
     * @return Une paire de nombre à factoriser. Le 1er paramètre est inferieur ou égal au 2eme paramètre
     */
    public Optional<Pair> factorisation(BigInteger n) {
        var tmp = n.sqrt();

        Optional<Pair> res = Optional.empty();
        final BigInteger max = n.add(BigInteger.ONE);
        for (BigInteger i = BigInteger.ZERO; i.compareTo(max) <= 0; i = i.add(BigInteger.ONE)) {
            var a = tmp.add(i);
            var tmp2 = a.pow(2).subtract(n);

            if(tmp2.compareTo(BigInteger.ZERO)<0) {
                // tmp2 <0 => on passe au suivant
            } else if(tmp2.equals(BigInteger.ZERO)){
                // trouvé => n est un carré
                res = Optional.of(new Pair(a, a));
                break;
            } else {
                var tmp3 = racineCaree(tmp2);
                if (!tmp3.equals(BigInteger.ZERO)) {
                    var b = tmp3;
                    var tmp4 = a.add(b);
                    var tmp5 = a.subtract(b);
                    if (BigInteger.ONE.equals(tmp4) || BigInteger.ONE.equals(tmp5)) {
                        LOGGER.atInfo().log("solution triviale: {}={}*{} (ignorée)", n, tmp4, tmp5);
                    } else {
                        LOGGER.info("trouve: {}= {} * {}", n, tmp4, tmp5);
                        if (tmp4.compareTo(tmp5) <= 0) {
                            res = Optional.of(new Pair(tmp4, tmp5));
                        } else {
                            res = Optional.of(new Pair(tmp5, tmp4));
                        }
                        break;
                    }
                }
            }
        }
        if (res.isEmpty()) {
            LOGGER.info("pas trouve pour {}", n);
        }
        return res;
    }

    /**
     * Retourne la racine carrée du nombre ou 0 si la racine carée n'est pas entiere
     *
     * @param carre Le carré ou il faut prendre la racine carré
     * @return La racine carée ou 0 si le paramétre n'est pas un carré
     */
    private BigInteger racineCaree(BigInteger carre) {
        if (OPTIMISE_VERIFICATION_CARRE) {
            var tmp = carre.mod(BI_100);
            if (!TERMINAISON_CARRE.contains(tmp)) {
                return BigInteger.ZERO;
            }
        }
        if(carre.compareTo(BigInteger.ZERO)<0){
            return BigInteger.ZERO;
        }
        var racineCarre = carre.sqrt();
        if (racineCarre.multiply(racineCarre).equals(carre)) {
            return racineCarre;
        } else {
            return BigInteger.ZERO;
        }
    }

}
