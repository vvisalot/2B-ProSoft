package Clases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;

public class Cromosoma { //un pedido
    private String idCamion; //solo necesario id (contiene tipo y con eso se ve capacidad)
    private HashMap<String, Tramo> rutaMap; //la ruta de un pedido
    private double tiempototal;  //suma de todas las horas de tramos --> fitness
    private double fitness; 
    
    public Cromosoma(String idCamion) {
        this.idCamion = idCamion;
    }

    //Constructor para generar las rutas
    public Cromosoma(HashMap<String, Tramo> rutaMap, Camion camion) {
        this.idCamion = camion.getIdCamion();
        this.rutaMap = rutaMap;
        this.fitness = 0.0;
        this.tiempototal = 0.0;
    }

    public String getIdCamion() {
        return idCamion;
    }

    public void setIdCamion(String idCamion) {
        this.idCamion = idCamion;
    }

    public Double getTiempoTotal() {
        return tiempototal;
    }

    public void setTiempoTotal(){
        double sum=0.0;
        for(Tramo tramo : rutaMap.values()){
            sum+=tramo.getHorasTramo();
            //System.out.println("Tiempo del tramo " + tramo.getUbigeoOrigen() + "-" + tramo.getUbigeoDestino() + ": " + tramo.getHorasTramo());
    
        }
        this.tiempototal = sum;
        //System.out.println("Tiempo total: " + sum);
    }

    public HashMap<String, Tramo> getRutaMap() {
        return rutaMap;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness() {
        double sumTiempo = 0.0;
        double sumDistancia = 0.0;

        for (Tramo tramo : rutaMap.values()) {
            // Sumar tiempo y distancia del tramo.
            sumTiempo += tramo.getHorasTramo();
            sumDistancia += tramo.getDistanciaTramo();
        }

        // Evitar división por 0: Asegúrate de que la distancia + tiempo no sea 0
        if (sumTiempo + sumDistancia > 0) {
            this.fitness = 1 / (sumTiempo + (sumDistancia * 0.5));  // Inversa de tiempo + distancia con una penalizacion del 50% para que tenga más peso el tiempo
        } else {
            this.fitness = Double.MAX_VALUE;  // Penalización si por alguna razón el tiempo y distancia son 0
        }
    }
    
}
