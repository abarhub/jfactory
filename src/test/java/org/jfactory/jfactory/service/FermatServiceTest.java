package org.jfactory.jfactory.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FermatServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FermatServiceTest.class);

    @Test
    void factorisation() {
        BigInteger n;

//        n=BigInteger.valueOf(799);
        //n=BigInteger.valueOf(15); // 3*5
//        n=BigInteger.valueOf(9409); // 97*97
//        n=BigInteger.valueOf(115); // 5*23
        n=BigInteger.valueOf(28741); // 41*701
//        n=BigInteger.valueOf(99400891); // 9967*9973
//        n=BigInteger.valueOf(2479541989L); // 49789*49801
//        n=BigInteger.valueOf(99998800003591L); // 9999937 * 9999943
//        n=new BigInteger("13615791439058800032819559173865300498204302398087",10)
//                .multiply(new BigInteger("24052778196359970728000014369293610757236829751899",10));
//        n=new BigInteger("4248200851",10)
//                .multiply(new BigInteger("6210319747",10)); // n =26382685634187504697, durée = 13s
//        n=new BigInteger("859139077199680238479671925831",10)
//                .multiply(new BigInteger("859139077199680238479671925831",10)); // trop long (>1h)





        FermatService fermatService=new FermatService();
        LOGGER.atInfo().log("Factorisation de {} ...",n);
        var debut= Instant.now();
        var res=fermatService.factorisation(n);

        if(res.isPresent()) {
            LOGGER.atInfo().log("n({})=a({})*b({})", n, res.get().a(),res.get().b());
        } else {
            LOGGER.atInfo().log("pas trouvé pour n({})", n);
        }
        LOGGER.atInfo().log("duree={}", Duration.between(debut,Instant.now()));
    }

    private static Stream<Arguments> provideTestFactorisation() {
        return Stream.of(
                Arguments.of(bi(15), bi(3),bi(5)),
                Arguments.of(bi(115), bi(5),bi(23)),
                Arguments.of(bi(799), bi(17),bi(47)),
                Arguments.of(bi(9409), bi(97),bi(97)),
                Arguments.of(bi(28741), bi(41),bi(701)),
                Arguments.of(bi(99400891), bi(9967),bi(9973)),
                Arguments.of(bi(2479541989L), bi(49789),bi(49801)),
                Arguments.of(bi(99998800003591L), bi(9999937),bi(9999943))
//                Arguments.of("", true),
//                Arguments.of("  ", true),
//                Arguments.of("not blank", false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestFactorisation")
    void testFactorisation(BigInteger n, BigInteger a,BigInteger b) {
//        BigInteger n;

//        n=BigInteger.valueOf(799);
        //n=BigInteger.valueOf(15); // 3*5
//        n=BigInteger.valueOf(9409); // 97*97
//        n=BigInteger.valueOf(115); // 5*23
//        n=BigInteger.valueOf(28741); // 41*701
//        n=BigInteger.valueOf(99400891); // 9967*9973
//        n=BigInteger.valueOf(2479541989L); // 49789*49801
//        n=BigInteger.valueOf(99998800003591L); // 9999937 * 9999943


        FermatService fermatService=new FermatService();
        var res=fermatService.factorisation(n);

        assertAll("Verifie multiplication",
                ()->assertTrue(res.isPresent()),
                ()->assertEquals(a, res.get().a()),
                ()->assertEquals(b, res.get().b()));
    }

    private static BigInteger bi(long n){
        return BigInteger.valueOf(n);
    }
}