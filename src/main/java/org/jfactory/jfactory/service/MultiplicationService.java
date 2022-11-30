package org.jfactory.jfactory.service;

import org.jfactory.jfactory.domain.*;
import org.jfactory.jfactory.runner.AppRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MultiplicationService {

    private static Logger LOGGER = LoggerFactory.getLogger(MultiplicationService.class);

    public Equation generationEquation(String nombre){

        //Verify

        List<Addition> additions=new ArrayList<>();
        List<Constante> nombres=new ArrayList<>();

        for(int i=0;i<nombre.length();i++){
            char c=nombre.charAt(i);

            int n=c-'0';
            List<Multiplication> liste=new ArrayList<>();

            for(int j=0;j<=i;j++){
                var x="x"+j;
                var y="y"+(i-j);
                liste.add(new Multiplication(new Variable(x),new Variable(y)));
            }

            var add=new Addition(liste,i,n);
            additions.add(add);

            nombres.add(new Constante(n));
        }

        return new Equation(additions,nombre,nombres);

    }

}
