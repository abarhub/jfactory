package org.jfactory.jfactory.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class FermatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FermatService.class);

    // factorisation de fermat : https://blogdemaths.wordpress.com/2014/05/18/savez-vous-factoriser-a-la-mode-de-fermat/
    public void factorisation(BigInteger n){
        var tmp=n.sqrt();

        boolean trouve=false;
        for(long i=1;i<=5000L;i++){
            var a=tmp.add(BigInteger.valueOf(i));
            var tmp2=a.pow(2).subtract(n);

            var tmp3=tmp.sqrt();
            if(tmp3.multiply(tmp3).equals(tmp2)){
                var b=tmp3;
                var tmp4=a.add(b);
                var tmp5=a.subtract(b);
                LOGGER.info("trouve: {}= {} * {}",n,tmp4,tmp5);
                trouve=true;
                break;
            }
        }
        if(!trouve){
            LOGGER.info("pas trouve pour {}",n);
        }
    }

}
