package org.jfactory.jfactory.listener;

import org.jfactory.jfactory.domain.Doublet;
import org.jfactory.jfactory.domain.Equation;
import org.jfactory.jfactory.domain.Resultat;
import org.jfactory.jfactory.domain.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class EnregistreParcourtListener implements ParcourtListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnregistreParcourtListener.class);

    private Map<Integer,Integer> mapX;

    private Map<Integer,Integer> mapY;

    private List<Map<String,String>> csv=new ArrayList<>();

    private final Equation equation;

    private long id;

    public EnregistreParcourtListener(Equation equation) {
        this.equation = equation;
        initialise();
    }

    private void initialise(){
        mapX=new TreeMap<>();
        id=1L;
    }

    @Override
    public void trouve(int ordre, Resultat resultat) {
        var map=csv.get(csv.size()-1);
        map.put("etat","1");
        map.put("solution","1");
    }

    @Override
    public void entre(int ordre) {
        Map<String,String> map=new LinkedHashMap<>();
        map.put("ordre",ordre+"");
        for(int i=0;i<equation.getNbVariable(true);i++){
            var v=equation.getVariable(true,i).get();
            if(v.isAffecte()){
                map.put(v.getNom(),v.getValeur()+"");
            } else {
                map.put(v.getNom(),"");
            }
        }
        for(int i=0;i<equation.getNbVariable(false);i++){
            var v=equation.getVariable(false,i).get();
            if(v.isAffecte()){
                map.put(v.getNom(),v.getValeur()+"");
            } else {
                map.put(v.getNom(),"");
            }
        }
        map.put("etat","0");
        map.put("solution","0");
        map.put("no",id+"");
        id++;

        csv.add(map);
    }

    @Override
    public void sort(int ordre) {

    }

    @Override
    public void variablesAAffecter(int ordre, List<Variable> listeVariables) {

    }

    @Override
    public void affecte(int ordre, List<Variable> listeVariables) {

    }

    @Override
    public void desaffecte(int ordre, List<Variable> listeVariables) {

    }

    @Override
    public void debut() {

    }

    @Override
    public void fin() {

    }

    @Override
    public void invalide(int ordre) {
        var map=csv.get(csv.size()-1);
        map.put("etat","2");
    }

    @Override
    public void valeurPossibles(int ordre, List<Variable> listeVariables, List<Doublet> listeValeursPossibles) {

    }

    public void writerFile(Path p) throws IOException {
        boolean premier=true;
        StringBuilder buf= new StringBuilder();
        for(var tmp:csv){
            if(premier){
                premier=false;
                var prem=true;
                for(var tmp2:tmp.entrySet()){
                    if(!prem){
                        buf.append(",");
                    }
                    prem=false;
                    buf.append("").append(tmp2.getKey());
                }
                buf.append("\n");
            }
            var prem=true;
            for(var tmp2:tmp.entrySet()){
                if(!prem){
                    buf.append(",");
                }
                prem=false;
                buf.append("").append(tmp2.getValue());
            }
            buf.append("\n");
        }
        Files.writeString(p, buf.toString());
    }
}
