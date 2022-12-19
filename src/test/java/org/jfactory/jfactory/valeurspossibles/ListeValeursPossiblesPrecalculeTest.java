package org.jfactory.jfactory.valeurspossibles;

import org.jfactory.jfactory.domain.Triplet;
import org.jfactory.jfactory.runner.AppRunner;
import org.jfactory.jfactory.service.MultiplicationService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

class ListeValeursPossiblesPrecalculeTest {

    private static Logger LOGGER = LoggerFactory.getLogger(ListeValeursPossiblesPrecalculeTest.class);

    @Test
    void normalisteNegatif() {
        // ARRANGE
        ListeValeursPossiblesPrecalcule listeValeursPossiblesPrecalcule = new ListeValeursPossiblesPrecalcule();

        // ACT
        var n = listeValeursPossiblesPrecalcule.normaliste(5, -8);

        // ASSERT
        assertEquals(7, n);
    }

    @Test
    void normaliste() {
        // ARRANGE
        ListeValeursPossiblesPrecalcule listeValeursPossiblesPrecalcule = new ListeValeursPossiblesPrecalcule();

        // ACT
        var n = listeValeursPossiblesPrecalcule.normaliste(4, 1);

        // ASSERT
        assertEquals(5, n);
    }

    @Test
    void normalisteZero() {
        // ARRANGE
        ListeValeursPossiblesPrecalcule listeValeursPossiblesPrecalcule = new ListeValeursPossiblesPrecalcule();

        // ACT
        var n = listeValeursPossiblesPrecalcule.normaliste(2, 0);

        // ASSERT
        assertEquals(2, n);
    }

    @Test
    void normalisteGrand() {
        // ARRANGE
        ListeValeursPossiblesPrecalcule listeValeursPossiblesPrecalcule = new ListeValeursPossiblesPrecalcule();

        // ACT
        var n = listeValeursPossiblesPrecalcule.normaliste(48, 76);

        // ASSERT
        assertEquals(4, n);
    }

    @Test
    void precalcul() {
        // ARRANGE
        ListeValeursPossiblesPrecalcule listeValeursPossiblesPrecalcule = new ListeValeursPossiblesPrecalcule();

        // ACT
        listeValeursPossiblesPrecalcule.precalcul();

        // ASSERT
        assertNotNull(listeValeursPossiblesPrecalcule.getMapDoulet());
        assertNotNull(listeValeursPossiblesPrecalcule.getMapTriple());
        assertNotNull(listeValeursPossiblesPrecalcule.getListe());

        var triplet = new Triplet(5, 3, 5);
        var res = listeValeursPossiblesPrecalcule.getMapTriple().get(triplet);
        assertNotNull(res);
        assertThat(res).hasSize(10)
                .extracting("x", "y")
                .contains(tuple(0, 5), tuple(1, 0), tuple(2, 5), tuple(3, 0),
                        tuple(4, 5), tuple(5, 0), tuple(6, 5), tuple(7, 0),
                        tuple(8, 5), tuple(9, 0));

        for (var t : res) {
            var n1 = triplet.getA() * t.getX() + triplet.getB() * t.getY();
            var n2 = triplet.getC();
            assertEquals(n1 % 10, n2 % 10, "n1=" + n1 + ",n2=" + n2);
        }
    }

    @Test
    void precalculComplet() {
        // ARRANGE
        ListeValeursPossiblesPrecalcule listeValeursPossiblesPrecalcule = new ListeValeursPossiblesPrecalcule();

        // ACT
        listeValeursPossiblesPrecalcule.precalcul();

        // ASSERT
        assertNotNull(listeValeursPossiblesPrecalcule.getMapDoulet());
        assertNotNull(listeValeursPossiblesPrecalcule.getMapTriple());
        assertNotNull(listeValeursPossiblesPrecalcule.getListe());

        for (var entry : listeValeursPossiblesPrecalcule.getMapTriple().entrySet()) {
            var triplet = entry.getKey();
            var res = entry.getValue();
            for (var t : res) {
                var n1 = triplet.getA() * t.getX() + triplet.getB() * t.getY();
                var n2 = triplet.getC();
                assertEquals(n1 % 10, n2 % 10, "n1=" + n1 + ",n2=" + n2);
            }
        }

        for (var entry : listeValeursPossiblesPrecalcule.getMapDoulet().entrySet()) {
            var n = entry.getKey();
            var res = entry.getValue();
            for (var t : res) {
                var n1 = t.getX() * t.getY();
                var n2 = n;
                assertEquals(n1 % 10, n2 % 10, "n1=" + n1 + ",n2=" + n2);
            }
        }

        var liste = listeValeursPossiblesPrecalcule.getListe();
        assertThat(liste).hasSize(10)
                .extracting("x")
                .contains(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

    }

    @Test
    void getListeValeurPossibles115() {
        // ARRANGE
        MultiplicationService multiplicationService = new MultiplicationService();
        var eq = multiplicationService.generationEquation("115");
        var x0 = eq.getVariable(true, 0).orElseThrow();
        var y0 = eq.getVariable(false, 0).orElseThrow();
        x0.setValeur(5);
        x0.setAffecte(true);
        y0.setValeur(1);
        y0.setAffecte(true);
        ListeValeursPossiblesPrecalcule listeValeursPossiblesPrecalcule = new ListeValeursPossiblesPrecalcule();
        multiplicationService.setListeValeursPossibles(listeValeursPossiblesPrecalcule);
        var add = eq.getEquationSimplifiee(1);

        // ACT
        var res = listeValeursPossiblesPrecalcule.getListeValeurPossibles(eq, 1, add.getVariablesNonAffectees(), add);

        // ASSERT
        assertNotNull(res);
        LOGGER.atInfo().log("eq={}",eq);
        LOGGER.atInfo().log("res={}",res);
//        LOGGER.atInfo().log("{}",res.stream());
        var tmp=res.stream().filter(x->x.getX()==1&&x.getY()==0).findFirst();
        assertTrue(tmp.isPresent(),"res="+res);

    }

}