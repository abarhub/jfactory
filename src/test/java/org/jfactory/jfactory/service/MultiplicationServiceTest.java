package org.jfactory.jfactory.service;

import org.jfactory.jfactory.domain.Multiplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

class MultiplicationServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void generationEquation15() {
        // ARRANGE
        MultiplicationService multiplicationService = new MultiplicationService();

        // ACT
        var res = multiplicationService.generationEquation("15");

        // ASSERT
        assertNotNull(res);
        assertEquals(2, res.getAdditions().size());
        assertEquals("15", res.getValeur());
        assertThat(res.getAdditions())
                .hasSize(2)
                .extracting("ordre", "valeur")
                .contains(tuple(0, 5), tuple(1, 1));
        var add = res.getAdditions().get(0);
        assertEquals(0, add.getOrdre());
        var listAdd = add.getAddition();
        assertEquals(1, listAdd.size());
        assertEquals("x0", ((Multiplication) listAdd.get(0)).getV1().getNom());
        assertEquals(0, ((Multiplication) listAdd.get(0)).getV1().getNo());
        assertEquals("y0", ((Multiplication) listAdd.get(0)).getV2().getNom());
        assertEquals(0, ((Multiplication) listAdd.get(0)).getV2().getNo());
        add = res.getAdditions().get(1);
        assertEquals(1, add.getOrdre());
        listAdd = add.getAddition();
        assertEquals(1, listAdd.size());
        assertEquals("x1", ((Multiplication) listAdd.get(0)).getV1().getNom());
        assertEquals(1, ((Multiplication) listAdd.get(0)).getV1().getNo());
        assertEquals("y0", ((Multiplication) listAdd.get(0)).getV2().getNom());
        assertEquals(0, ((Multiplication) listAdd.get(0)).getV2().getNo());
    }

    @Test
    void resolution115() {
        // ARRANGE
        MultiplicationService multiplicationService = new MultiplicationService();
        var eq = multiplicationService.generationEquation("115");

        // ACT
        var res = multiplicationService.resolution(eq);

        // ASSERT
        assertNotNull(res);
        assertThat(res).hasSize(3)
                .extracting("valeurBi", "x", "y")
                .contains(tuple(BigInteger.valueOf(115), BigInteger.valueOf(115), BigInteger.valueOf(1)),
                        tuple(BigInteger.valueOf(115), BigInteger.valueOf(23), BigInteger.valueOf(5)),
                        tuple(BigInteger.valueOf(115), BigInteger.valueOf(5), BigInteger.valueOf(23)));
    }

    @Test
    void resolution15() {
        // ARRANGE
        MultiplicationService multiplicationService = new MultiplicationService();
        var eq = multiplicationService.generationEquation("15");

        // ACT
        var res = multiplicationService.resolution(eq);

        // ASSERT
        assertNotNull(res);
        assertThat(res).hasSize(3)
                .extracting("valeurBi", "x", "y")
                .contains(tuple(BigInteger.valueOf(15), BigInteger.valueOf(15), BigInteger.valueOf(1)),
                        tuple(BigInteger.valueOf(15), BigInteger.valueOf(3), BigInteger.valueOf(5)),
                        tuple(BigInteger.valueOf(15), BigInteger.valueOf(5), BigInteger.valueOf(3)));
    }

    @Test
    void resolution9409() {
        // ARRANGE
        MultiplicationService multiplicationService = new MultiplicationService();
        var eq = multiplicationService.generationEquation("9409");

        // ACT
        var res = multiplicationService.resolution(eq);

        // ASSERT
        assertNotNull(res);
        assertThat(res).hasSize(2)
                .extracting("valeurBi", "x", "y")
                .contains(tuple(BigInteger.valueOf(9409), BigInteger.valueOf(9409), BigInteger.valueOf(1)),
                        tuple(BigInteger.valueOf(9409), BigInteger.valueOf(97), BigInteger.valueOf(97)));
    }

    @Test
    void resolution28741() {
        // ARRANGE
        MultiplicationService multiplicationService = new MultiplicationService();
        var eq = multiplicationService.generationEquation("28741");

        // ACT
        var res = multiplicationService.resolution(eq);

        // ASSERT
        assertNotNull(res);
        assertThat(res).hasSize(3)
                .extracting("valeurBi", "x", "y")
                .contains(tuple(BigInteger.valueOf(28741), BigInteger.valueOf(28741), BigInteger.valueOf(1)),
                        tuple(BigInteger.valueOf(28741), BigInteger.valueOf(701), BigInteger.valueOf(41)),
                        tuple(BigInteger.valueOf(28741), BigInteger.valueOf(41), BigInteger.valueOf(701)));
    }

}