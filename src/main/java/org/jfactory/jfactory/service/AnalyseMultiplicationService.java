package org.jfactory.jfactory.service;

import org.jfactory.jfactory.domain.Equation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class AnalyseMultiplicationService {

    private static Logger LOGGER = LoggerFactory.getLogger(AnalyseMultiplicationService.class);

    record Mult(long a,long b,long n) {}

    public void run() {
        LOGGER.atInfo().log("Analyse multiplication ...");

        try {
            Path p = Path.of("files/double_primes_5.txt");
            var liste = Files.lines(p)
                    .map(x->{
                        var tmp=x.split("=");
                        var n=tmp[1];
                        var tmp2=tmp[0].split("\\*");
                        var a=tmp2[0];
                        var b=tmp2[1];
                        var n0=Long.parseLong(n,10);
                        var a0=Long.parseLong(a,10);
                        var b0=Long.parseLong(b,10);
                        return new Mult(a0,b0,n0);
                    })
                    .filter(x->x.n>=1000)
                    .filter(x->getValeurPos(x.n,2)==5)
                    .collect(Collectors.toList());

            LOGGER.atInfo().log("liste={}",liste);
            LOGGER.atInfo().log("size(liste)={}",liste.size());

            var tmp=liste.get(0);

            MultiplicationService multiplicationService=new MultiplicationService();

            var eq=multiplicationService.generationEquation(tmp.n+"");

            LOGGER.atInfo().log("eq={}",eq);

            affectation(eq,true, tmp.a());
            affectation(eq,false, tmp.b);
//            for(int i=0;i<eq.getNbVariable(true);i++){
//                var v=eq.getVariable(true,i);
//                Assert.isTrue(v.isPresent(),"Variable "+i+" absente");
//                var v2=v.get();
//                var tmp2=getValeurPos(tmp.a,i);
//                LOGGER.atInfo().log("x{}:{}({})",i,tmp2,v2);
//                v2.setAffecte(true);
//                v2.setValeur(tmp2);
//            }

            LOGGER.atInfo().log("eq={}",eq);

            var tmp2=eq.getEquationSimplifiee(2);

            LOGGER.atInfo().log("eq ordre 3={}",tmp2);

            var x2=eq.getVariable(true,2);
            var y2=eq.getVariable(false,2);

            var valx2=x2.get().getValeur();
            var valy2=y2.get().getValeur();

            x2.get().setAffecte(false);
            x2.get().setValeur(-1);
            y2.get().setAffecte(false);
            y2.get().setValeur(-1);

            LOGGER.atInfo().log("eq ordre no {} : {}",2, eq.getEquationSimplifiee(2));

        }catch (Exception e){
            LOGGER.atError().log("Erreur",e);
        }
    }

    private void affectation(Equation eq,boolean x,long valeur){

        for(int i=0;i<eq.getNbVariable(x);i++){
            var v=eq.getVariable(x,i);
            Assert.isTrue(v.isPresent(),"Variable "+i+" absente");
            var v2=v.get();
            var tmp2=getValeurPos(valeur,i);
            LOGGER.atInfo().log("{}{}:{}({})",(x)?"x":"y",i,tmp2,v2);
            v2.setAffecte(true);
            v2.setValeur(tmp2);
        }
    }

    private int getValeurPos(long n,int pos){
        long tmp=1;
        for(int i=0;i<pos;i++){
            tmp=tmp*10;
        }
        return (int)((n/tmp)%10);
    }
}
