package org.jfactory.jfactory.iterator;

import java.math.BigInteger;
import java.util.*;

public class CarreIterator implements Iterator<BigInteger> {

    private static final Set<BigInteger> TERMINAISON_CARRE = Set.of(BigInteger.valueOf(0),
            BigInteger.valueOf(1), BigInteger.valueOf(4), BigInteger.valueOf(9), BigInteger.valueOf(16),
            BigInteger.valueOf(21), BigInteger.valueOf(24), BigInteger.valueOf(25), BigInteger.valueOf(29),
            BigInteger.valueOf(36), BigInteger.valueOf(41), BigInteger.valueOf(44), BigInteger.valueOf(49),
            BigInteger.valueOf(56), BigInteger.valueOf(61), BigInteger.valueOf(64), BigInteger.valueOf(69),
            BigInteger.valueOf(76), BigInteger.valueOf(81), BigInteger.valueOf(84), BigInteger.valueOf(89),
            BigInteger.valueOf(96));

    private static final BigInteger N100=BigInteger.TEN.pow(2);

    private final List<Integer> liste;

    private final Map<Integer, Integer> mapOrdre;
    private BigInteger current;

    private final BigInteger lastValue;

    private boolean ended;

    public CarreIterator(BigInteger current, BigInteger lastValue) {

        this.lastValue = lastValue;
        ended = false;
        liste = TERMINAISON_CARRE.stream()
                .map(x -> x.intValueExact())
                .sorted()
                .toList();
        mapOrdre = new HashMap<>();
        for (int i = 0; i < liste.size(); i++) {
            mapOrdre.put(liste.get(i), i);
        }
        if(estCarre(current)){
            this.current = current;
        } else {
            this.current = nextValue(current);
        }
    }

    @Override
    public boolean hasNext() {
        return current.compareTo(lastValue) <= 0;
    }

    @Override
    public BigInteger next() {
        if (current.compareTo(lastValue) <= 0) {
            var n = current;
            current = nextValue(this.current);
            return n;
        } else {
            throw new NoSuchElementException();
        }
    }

    private BigInteger nextValue(BigInteger n) {
        var n2 = n.mod(N100).intValueExact();
        if (liste.contains(n2)) {
            int diff;
            var tmp = mapOrdre.get(n2);
            if (tmp < liste.size() - 1) {
                var tmp2 = liste.get(tmp + 1);
                diff = tmp2 - n2;
            } else {
                diff = 100 - liste.get(tmp);
            }
            return n.add(BigInteger.valueOf(diff));
        } else {
            for (int i = 0; i < liste.size(); i++) {
                int n3=liste.get(i);
                if (n3 > n2) {
                    var diff = n3 - n2;
                    return n.add(BigInteger.valueOf(diff));
                }
            }
            var diff = 100 - n2;
            return n.add(BigInteger.valueOf(diff));
        }
    }

    private boolean estCarre(BigInteger current) {
        return TERMINAISON_CARRE.contains(current.mod(N100));
    }
}
