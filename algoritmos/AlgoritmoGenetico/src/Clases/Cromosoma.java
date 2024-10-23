package Clases;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

//1 solución al problema --> para un día?
public class Cromosoma { //un pedido
    private String idCamion; //solo necesario id (contiene tipo y con eso se ve capacidad)
    private HashMap<String, Tramo> rutaMap; //la ruta de un pedido
    private double tiempototal;  //suma de todas las horas de tramos --> fitness


    //otra idea
    //private ArrayList<ArrayList<String>> destinos;
    //private List<PlanTransporteCamion> planesTransporteCamion;
    //private double fitness;
    
    public Cromosoma(String idCamion) {
        this.idCamion = idCamion;
    }

    //Constructor para generar las rutas
    public Cromosoma(HashMap<String, Tramo> rutaMap, Camion camion) {
        this.idCamion = camion.getIdCamion();
        this.rutaMap = rutaMap;
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
    
}
