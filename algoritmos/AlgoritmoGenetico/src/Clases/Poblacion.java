package Clases;
import Clases.Cromosoma;
import java.util.List;

//Lista de tramos
public class Poblacion {
    private int tamanioPoblacion;
    private double probabilidadMutacion;
    private int generacion;
    private List<Cromosoma> listaCromosomas;    

    public Poblacion(int tamanioPoblacion, double probabilidadMutacion, int generacion, List<Cromosoma> listaCromosomas) {
        this.tamanioPoblacion = tamanioPoblacion;
        this.probabilidadMutacion = probabilidadMutacion;
        this.generacion = generacion;
        this.listaCromosomas = listaCromosomas;
    }
    
    
    public int getTamanioPoblacion() {
        return tamanioPoblacion;
    }

    public void setTamanioPoblacion(int tamanioPoblacion) {
        this.tamanioPoblacion = tamanioPoblacion;
    }

    public double getProbabilidadMutacion() {
        return probabilidadMutacion;
    }

    public void setProbabilidadMutacion(double probabilidadMutacion) {
        this.probabilidadMutacion = probabilidadMutacion;
    }

    public int getGeneracion() {
        return generacion;
    }

    public void setGeneracion(int generacion) {
        this.generacion = generacion;
    }

    public List<Cromosoma> getListaCromosomas() {
        return listaCromosomas;
    }

    public void setListaCromosomas(List<Cromosoma> listaCromosomas) {
        this.listaCromosomas = listaCromosomas;
    }
    
    
}
