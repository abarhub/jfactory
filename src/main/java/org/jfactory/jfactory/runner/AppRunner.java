package org.jfactory.jfactory.runner;

import org.jfactory.jfactory.service.MultiplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class AppRunner implements ApplicationRunner {

    private static Logger LOGGER = LoggerFactory.getLogger(AppRunner.class);

    @Autowired
    private MultiplicationService multiplicationService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("Run ...");

        String n;
//        n = "15"; // 3*5
//        n="9409"; // 97*97
//        n = "115"; // 5*23
        n = "28741"; // 41*701
//        n = "99400891"; // 9967*9973
//        n = "2479541989"; //49789*49801


        var eq = multiplicationService.generationEquation(n);

        LOGGER.info("eq={}", eq);

        Instant debut = Instant.now();

        multiplicationService.resolution(eq);

        Duration duree = Duration.between(debut, Instant.now());
        LOGGER.atInfo().addKeyValue("duree", duree).log("fin (duree:{})", duree);
    }
}
