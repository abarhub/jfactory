package org.jfactory.jfactory.service;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ParallelPortfolio;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMin;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;
import org.chocosolver.solver.variables.IntVar;
import org.jfactory.jfactory.domain.Constante;
import org.jfactory.jfactory.domain.Equation;
import org.jfactory.jfactory.domain.Multiplication;
import org.jfactory.jfactory.domain.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ResolutionCsp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResolutionCsp.class);

    private final MultiplicationService multiplicationService;

    public ResolutionCsp(MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }

    public void resolution(String nombre,String dureeLimit) {

        var equation=multiplicationService.generationEquation(nombre);

        LOGGER.info("eq: {}",equation);

        int limitReste;

//        limitReste=999;
        limitReste=99;


        var xMax=equation.getNbVariable(true);
        var yMax=equation.getNbVariable(false);

        Map<String,IntVar> map=new HashMap<>();
        Map<String, Variable> map2=new HashMap<>();
        Map<String,IntVar> map3=new HashMap<>();

        Model model = createModel(equation, limitReste, xMax, yMax, map, map2, map3);

        LOGGER.info("model : {}",model);

        LOGGER.info("recherche de la solution ...");
        Solver solver = model.getSolver();

        if(StringUtils.hasText(dureeLimit)) {
            solver.limitTime(dureeLimit);
        }

        var tmp5=new ArrayList<>(map.values());
//        tmp5.addAll(map3.values());
        solver.setSearch(Search.intVarSearch(
                // selects the variable of smallest domain size
                new FirstFail(model),
                // selects the smallest domain value (lower bound)
                new IntDomainMin(),
                // variables to branch on
                tmp5.toArray(new IntVar[0])
        ));

        int nbSolution=0;
        while(solver.solve()){
            nbSolution++;
            LOGGER.info("solution trouvée pour {} :", nombre);
            LOGGER.info("map: {}",map);
            LOGGER.info("map3: {}",map3);
            BigInteger x=BigInteger.ZERO;
            for(int i=0;i<xMax;i++){
                x=x.add(BigInteger.valueOf(map.get("x"+i).getValue()).multiply(BigInteger.TEN.pow(i)));
            }
            BigInteger y=BigInteger.ZERO;
            for(int i=0;i<yMax;i++){
                y=y.add(BigInteger.valueOf(map.get("y"+i).getValue()).multiply(BigInteger.TEN.pow(i)));
            }
            LOGGER.info("x: {}",x);
            LOGGER.info("y: {}",y);
        }

        if(nbSolution>0){
            LOGGER.info("nombre de solutions  trouvée: {}",nbSolution);
        } else {
            LOGGER.info("solution non trouvée");
        }

        solver.printStatistics();

//        if(solver.solve()){
//            LOGGER.info("solution trouvée: {}",solver.defaultSolution());
//            LOGGER.info("map: {}",map);
//            LOGGER.info("map3: {}",map3);
//            // do something, e.g. print out variable values
////        }else if(solver.hasReachedLimit()){
////            System.out.println("The solver could not find a solution
////                    nor prove that none exists in the given limits");
//        }else {
////            System.out.println("The solver has proved the problem has no solution");
//            LOGGER.info("solution non trouvée");
//        }

    }

    public void resolutionParallel(String nombre,int nbThread) {

        var equation=multiplicationService.generationEquation(nombre);

        LOGGER.info("eq: {}",equation);

        int limitReste;

//        limitReste=999;
        limitReste=99;


        var xMax=equation.getNbVariable(true);
        var yMax=equation.getNbVariable(false);

//        LOGGER.info("model : {}",model);

        LOGGER.info("recherche de la solution ...");
//        Solver solver = model.getSolver();

        ParallelPortfolio portfolio = new ParallelPortfolio();
        int nbModels = nbThread;
        for(int s=0;s<nbModels;s++){

            Map<String,IntVar> map=new HashMap<>();
            Map<String, Variable> map2=new HashMap<>();
            Map<String,IntVar> map3=new HashMap<>();

            Model model = createModel(equation, limitReste, xMax, yMax, map, map2, map3);

            portfolio.addModel(model);
        }
//        portfolio.solve();

//        if(StringUtils.hasText(dureeLimit)) {
//            solver.limitTime(dureeLimit);
//        }

//        var tmp5=new ArrayList<>(map.values());
//        tmp5.addAll(map3.values());
//        solver.setSearch(Search.intVarSearch(
//                // selects the variable of smallest domain size
//                new FirstFail(model),
//                // selects the smallest domain value (lower bound)
//                new IntDomainMin(),
//                // variables to branch on
//                tmp5.toArray(new IntVar[0])
//        ));

        int nbSolution=0;
        while(portfolio.solve()){
            nbSolution++;
            LOGGER.info("solution trouvée pour {} :", nombre);
//            LOGGER.info("map: {}",map);
//            LOGGER.info("map3: {}",map3);
//            BigInteger x=BigInteger.ZERO;
//            for(int i=0;i<xMax;i++){
//                x=x.add(BigInteger.valueOf(map.get("x"+i).getValue()).multiply(BigInteger.TEN.pow(i)));
//            }
//            BigInteger y=BigInteger.ZERO;
//            for(int i=0;i<yMax;i++){
//                y=y.add(BigInteger.valueOf(map.get("y"+i).getValue()).multiply(BigInteger.TEN.pow(i)));
//            }
//            LOGGER.info("x: {}",x);
//            LOGGER.info("y: {}",y);
        }

        if(nbSolution>0){
            LOGGER.info("nombre de solutions  trouvée: {}",nbSolution);
        } else {
            LOGGER.info("solution non trouvée");
        }

//        portfolio.getBestModel().printStatistics();

    }

    private static Model createModel(Equation equation, int limitReste, long xMax, long yMax,
                                     Map<String, IntVar> map, Map<String, Variable> map2,
                                     Map<String, IntVar> map3) {
        Model model = new Model("factory");

        for(int i = 0; i< xMax; i++){
            var vOpt= equation.getVariable(true,i);
            Assert.state(vOpt.isPresent(),"variable x"+i+" inexistante");
            var v=vOpt.get();
            IntVar v0 = model.intVar(v.getNom(), 0,9);
            map.put(v.getNom(),v0);
            map2.put(v.getNom(),v);
        }


        for(int i = 0; i< yMax; i++){
            var vOpt= equation.getVariable(false,i);
            Assert.state(vOpt.isPresent(),"variable y"+i+" inexistante");
            var v=vOpt.get();
            IntVar v0 = model.intVar(v.getNom(), 0,9);
            map.put(v.getNom(),v0);
            map2.put(v.getNom(),v);
        }

        ArExpression rest=null;
        String rest2=null;
        for(int i = 0; i< equation.getMax(); i++){
            var add= equation.getEquationSimplifiee(i);
            ArExpression res=null;
            for(var tmp:add.getAddition()){
                if(tmp instanceof Multiplication m){
                    var v=m.getV1();
                    var v2=m.getV2();
                    var tmp2= Objects.requireNonNull(map.get(v.getNom()));
                    var tmp3= Objects.requireNonNull(map.get(v2.getNom()));
                    var tmp4=tmp2.mul(tmp3);
                    if(res==null){
                        res=tmp4;
                    } else {
                        res=res.add(tmp4);
                    }
                } else if(tmp instanceof Constante c){
                    Assert.state(false,"Erreur");
                } else {
                    Assert.state(false,"Erreur");
                }
            }
            Assert.state(res!=null,"Erreur");

            if(rest2!=null){
                res=res.add(map3.get(rest2));
            }

            if(i< equation.getMax()-1) {
                var tmp5 = res.mod(10).eq(add.getValeur());
                tmp5.decompose().post();

//            model.post(res);

                LOGGER.info("res {}: {}", i, tmp5);

                var tmp6 = res.div(BigInteger.TEN.pow(1).intValueExact());

                rest2 = "rest" + i;
                IntVar v0 = model.intVar(rest2, 0, limitReste);
                var tmp7 = tmp6.eq(v0);
                tmp7.decompose().post();
                map3.put(rest2, v0);
                LOGGER.info("rest {}: {}", i, tmp7);
            } else {

                var tmp5 = res.eq(add.getValeur());
                tmp5.decompose().post();

                LOGGER.info("res {}: {}", i, tmp5);

                rest2=null;
            }


//            break;
//            rest=tmp6;
        }
        return model;
    }
}
