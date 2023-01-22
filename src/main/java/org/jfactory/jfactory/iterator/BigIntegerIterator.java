package org.jfactory.jfactory.iterator;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BigIntegerIterator implements Iterator<BigInteger> {

    private BigInteger current;

    private final BigInteger lastValue;

    public BigIntegerIterator(BigInteger current, BigInteger lastValue) {
        this.current = current;
        this.lastValue = lastValue;
    }

    @Override
    public boolean hasNext() {
        return current.compareTo(lastValue) <= 0;
    }

    @Override
    public BigInteger next() {
        if (current.compareTo(lastValue) <= 0) {
            var value = current;
            current = current.add(BigInteger.ONE);
            return value;
        } else {
            throw new NoSuchElementException();
        }
    }
}
