package org.jfactory.jfactory.runner;

import org.jfactory.jfactory.service.MultiplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Service
public class AppRunner implements ApplicationRunner {

    private static Logger LOGGER = LoggerFactory.getLogger(AppRunner.class);

    @Autowired
    private MultiplicationService multiplicationService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("Run ...");

        String n;
        n = "15";

        var eq = multiplicationService.generationEquation(n);

        LOGGER.info("eq={}", eq);
    }
}
