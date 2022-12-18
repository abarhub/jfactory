package org.jfactory.jfactory.service;

import org.jfactory.jfactory.domain.Multiplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

class MultiplicationServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void generationEquation15() {
        MultiplicationService multiplicationService=new MultiplicationService();
        var res=multiplicationService.generationEquation("15");
        assertNotNull(res);
        assertEquals(2,res.getAdditions().size());
        assertEquals("15",res.getValeur());
        assertThat(res.getAdditions())
                .hasSize(2)
                .extracting("ordre","valeur")
                .contains(tuple(0,5),tuple(1,1));
        var add=res.getAdditions().get(0);
        assertEquals(0,add.getOrdre());
        var listAdd=add.getAddition();
        assertEquals(1,listAdd.size());
        assertEquals("x0",((Multiplication)listAdd.get(0)).getV1().getNom());
        assertEquals(0,((Multiplication)listAdd.get(0)).getV1().getNo());
        assertEquals("y0",((Multiplication)listAdd.get(0)).getV2().getNom());
        assertEquals(0,((Multiplication)listAdd.get(0)).getV2().getNo());
        add=res.getAdditions().get(1);
        assertEquals(1,add.getOrdre());
        listAdd=add.getAddition();
        assertEquals(1,listAdd.size());
        assertEquals("x1",((Multiplication)listAdd.get(0)).getV1().getNom());
        assertEquals(1,((Multiplication)listAdd.get(0)).getV1().getNo());
        assertEquals("y0",((Multiplication)listAdd.get(0)).getV2().getNom());
        assertEquals(0,((Multiplication)listAdd.get(0)).getV2().getNo());
    }

    @Test
    void resolution() {
    }
}