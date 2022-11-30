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
            char c=nombre.charAt(nombre.length()-i-1);

            int n=c-'0';
            List<Operation> liste=new ArrayList<>();

            for(int j=0;j<=i;j++){
                var x="x"+j;
                var y="y"+(i-j);
                liste.add(new Multiplication(new Variable(x,j),new Variable(y,i-j)));
            }

            var add=new Addition(liste,i,n);
            additions.add(add);

            nombres.add(new Constante(n));
        }

        return new Equation(additions,nombre,nombres);
    }

    public void resolution(Equation equation){


        List<List<Integer>> listeValeursPossibles=new ArrayList<>();
        for(var i=0;i<10;i++){
            for(int j=0;j<10;j++){
                List<Integer> liste=new ArrayList<>();
                liste.add(i);
                liste.add(j);
                listeValeursPossibles.add(liste);
            }
        }

        for(var i=0;i<equation.getAdditions().size();i++){
            var ordre=i;

            var add=equation.getEquationSimplifiee(ordre);
            LOGGER.info("ordre {} : {}", ordre,add);


        }

    }

    private void resolution2(Equation equation, int ordre, int max, List<List<Integer>> listeValeursPossibles){

        var eq=equation.getEquationSimplifiee(ordre);



        for(var l : listeValeursPossibles){

        }
    }

}
