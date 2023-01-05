package org.jfactory.jfactory.runner;

import org.jfactory.jfactory.listener.ParcourtLog;
import org.jfactory.jfactory.service.FermatService;
import org.jfactory.jfactory.service.MultiplicationService;
import org.jfactory.jfactory.service.ResolutionCsp;
import org.jfactory.jfactory.valeurspossibles.ListeValeursPossiblesPrecalcule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;

@Service
public class AppRunner implements ApplicationRunner {

    private static Logger LOGGER = LoggerFactory.getLogger(AppRunner.class);

    @Autowired
    private MultiplicationService multiplicationService;

    @Autowired
    private ResolutionCsp resolutionCsp;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int methode;
//        methode=1;
//        methode=2;
        methode=3;
        if(methode==1) {
            resolution();
        } else if(methode==2) {
            resolutionCsp();
        } else if(methode==3){
            factorisationFermat();
        }
    }

    private void factorisationFermat(){
        FermatService fermatService=new FermatService();

        BigInteger n;
        n=BigInteger.valueOf(115);

        var res=fermatService.factorisation(n);
        LOGGER.info("{}={}",n,res);
    }

    private void resolutionCsp() {
        LOGGER.info("Run CSP ...");

        String n;
//        n = "15"; // 3*5
//        n="9409"; // 97*97
//        n = "115"; // 5*23
//        n = "28741"; // 41*701
//        n = "99400891"; // 9967*9973
        n = "2479541989"; //49789*49801
//        n = "99998800003591"; //9999937 * 9999943


        Instant debut = Instant.now();

        resolutionCsp.resolution(n);

        Duration duree = Duration.between(debut, Instant.now());
        LOGGER.atInfo().addKeyValue("duree", duree).log("fin (duree:{})", duree);
    }

    public void resolution(){
        LOGGER.info("Run ...");

        String n;
//        n = "15"; // 3*5
//        n="9409"; // 97*97
//        n = "115"; // 5*23
        n = "28741"; // 41*701
//        n = "99400891"; // 9967*9973
//        n = "2479541989"; //49789*49801
//        n = "99998800003591"; //9999937 * 9999943


        var eq = multiplicationService.generationEquation(n);

        LOGGER.info("eq={}", eq);

//        LOGGER.atInfo()
//                .addKeyValue("oldT", 1)
//                .addKeyValue("newT", 2)
//                .log("Temperature changed.");

        if (true) {
            multiplicationService.ajouteListener(new ParcourtLog(eq));
        }

        if(true){
            multiplicationService.setListeValeursPossibles(new ListeValeursPossiblesPrecalcule());
        }

        Instant debut = Instant.now();

        multiplicationService.resolution(eq);

        Duration duree = Duration.between(debut, Instant.now());
        LOGGER.atInfo().addKeyValue("duree", duree).log("fin (duree:{})", duree);
    }
}
