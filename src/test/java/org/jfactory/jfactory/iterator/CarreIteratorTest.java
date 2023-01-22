package org.jfactory.jfactory.iterator;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarreIteratorTest {

    @Test
    void next0to4() {

        int debut = 0;
        int fin = 4;
        List<Integer> liste = List.of(0, 1, 4);

        CarreIterator iterator = new CarreIterator(BigInteger.valueOf(debut), BigInteger.valueOf(fin));

        for (int i : liste) {
            BigInteger bi = BigInteger.valueOf(i);
            assertTrue(iterator.hasNext());
            assertEquals(bi, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    void next0to49() {

        int debut = 0;
        int fin = 49;
        List<Integer> liste = List.of(0, 1, 4, 9, 16, 21, 24, 25, 29, 36, 41, 44, 49);

        CarreIterator iterator = new CarreIterator(BigInteger.valueOf(debut), BigInteger.valueOf(fin));

        for (int i : liste) {
            BigInteger bi = BigInteger.valueOf(i);
            assertTrue(iterator.hasNext());
            assertEquals(bi, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    void next0to104() {

        int debut = 0;
        int fin = 104;
        List<Integer> liste = List.of(0, 1, 4, 9, 16, 21, 24, 25, 29, 36, 41, 44, 49,56,61,64,69,76,81,84,89,96,100,101,104);

        CarreIterator iterator = new CarreIterator(BigInteger.valueOf(debut), BigInteger.valueOf(fin));

        for (int i : liste) {
            BigInteger bi = BigInteger.valueOf(i);
            assertTrue(iterator.hasNext());
            assertEquals(bi, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    void next16to56() {

        int debut = 16;
        int fin = 56;
        List<Integer> liste = List.of( 16, 21, 24, 25, 29, 36, 41, 44, 49,56);

        CarreIterator iterator = new CarreIterator(BigInteger.valueOf(debut), BigInteger.valueOf(fin));

        for (int i : liste) {
            BigInteger bi = BigInteger.valueOf(i);
            assertTrue(iterator.hasNext());
            assertEquals(bi, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    void next12to21() {

        int debut = 12;
        int fin = 21;
        List<Integer> liste = List.of( 16, 21);

        CarreIterator iterator = new CarreIterator(BigInteger.valueOf(debut), BigInteger.valueOf(fin));

        for (int i : liste) {
            BigInteger bi = BigInteger.valueOf(i);
            assertTrue(iterator.hasNext());
            assertEquals(bi, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

}