/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicExtraction.TETConfigurations;

import topicExtraction.TETParameters.Parameters_PLSA;
import java.io.Serializable;

/**
 *
 * @author Gaia
 */
@SuppressWarnings("serial")
public class TopicExtractionParameters implements Serializable{
 
    private Integer numTopics;
    
    private String dirEntrada;
    private String dirSaida;
    
    private boolean PLSA;
    
    Parameters_PLSA parametersPLSA;
    
    public TopicExtractionParameters(){
        setDirEntrada("");
        setDirSaida("");
        
        setPLSA(false);
        setParametersPLSA(new Parameters_PLSA());
        
    }
    
    public boolean isPLSA(){
        return this.PLSA;
    }
    
    public Parameters_PLSA getParametersPLSA(){
        return this.parametersPLSA;
    }
    
    public String getDirEntrada() {
        return dirEntrada;
    }

    public String getDirSaida() {
        return dirSaida;
    }
    
    public Integer getNumTopics(){
        return this.numTopics;
    }
    
    public void setParametersPLSA(Parameters_PLSA parameters){
        this.parametersPLSA = parameters;
    }
    
    public void setNumTopics(int numTopics){
        this.numTopics = numTopics;
    }
    
    public void setDirEntrada(String dirEntrada) {
        this.dirEntrada = dirEntrada;
    }

    public void setDirSaida(String dirSaida) {
        this.dirSaida = dirSaida;
    }
    
    public void setPLSA(boolean PLSA){
        this.PLSA = PLSA;
    }
}
