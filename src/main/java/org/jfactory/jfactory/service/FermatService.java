package org.jfactory.jfactory.service;

import org.jfactory.jfactory.domain.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Optional;

public class FermatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FermatService.class);

    // factorisation de fermat : https://blogdemaths.wordpress.com/2014/05/18/savez-vous-factoriser-a-la-mode-de-fermat/
    public Optional<Pair> factorisation(BigInteger n) {
        var tmp = n.sqrt();

        Optional<Pair> res = Optional.empty();
        final long max = n.add(BigInteger.ONE).longValueExact();
        for (long i = 1; i <= max; i++) {
            var a = tmp.add(BigInteger.valueOf(i));
            var tmp2 = a.pow(2).subtract(n);

            var tmp3 = tmp2.sqrt();
            if (tmp3.multiply(tmp3).equals(tmp2)) {
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
        if (res.isEmpty()) {
            LOGGER.info("pas trouve pour {}", n);
        }
        return res;
    }

}
