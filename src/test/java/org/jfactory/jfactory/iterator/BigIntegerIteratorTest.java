package org.jfactory.jfactory.iterator;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class BigIntegerIteratorTest {

    @Test
    void next0to5() {

        BigIntegerIterator iterator = new BigIntegerIterator(BigInteger.valueOf(0), BigInteger.valueOf(5));

        for (BigInteger i = BigInteger.ZERO; i.compareTo(BigInteger.valueOf(5)) <= 0; i = i.add(BigInteger.ONE)) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    void next5to20() {

        int debut = 5;
        int fin = 20;

        BigIntegerIterator iterator = new BigIntegerIterator(BigInteger.valueOf(debut), BigInteger.valueOf(fin));

        for (BigInteger i = BigInteger.valueOf(debut); i.compareTo(BigInteger.valueOf(fin)) <= 0; i = i.add(BigInteger.ONE)) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }


    @Test
    void next16to300() {

        int debut = 16;
        int fin = 300;

        BigIntegerIterator iterator = new BigIntegerIterator(BigInteger.valueOf(debut), BigInteger.valueOf(fin));

        for (BigInteger i = BigInteger.valueOf(debut); i.compareTo(BigInteger.valueOf(fin)) <= 0; i = i.add(BigInteger.ONE)) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }
}