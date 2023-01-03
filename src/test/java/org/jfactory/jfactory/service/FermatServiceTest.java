package org.jfactory.jfactory.service;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class FermatServiceTest {

    @Test
    void factorisation() {
        BigInteger n;

        //n=BigInteger.valueOf(799);
        n=BigInteger.valueOf(15);

        FermatService fermatService=new FermatService();
        fermatService.factorisation(n);
    }
}