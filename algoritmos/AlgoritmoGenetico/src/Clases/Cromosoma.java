package Clases;

import java.util.List;
public class Cromosoma {
    private String idCamion; //solo necesario id (contiene tipo y con eso se ve capacidad)
    private List<Tramo> ruta; //todas las posibles tutas de un pedido
    private double tiempototal;  //suma de todas las horas de tramos --> fitness
    
    public Cromosoma(String idCamion) {
        this.idCamion = idCamion;
    }

    //Constructor para generar las rutas
    public Cromosoma(List<Tramo> ruta2, Camion camion) {
        this.idCamion = camion.getIdCamion();
        this.ruta = ruta2;
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
        for(Tramo tram: this.ruta)
            sum+=tram.getHorasTramo();
        this.tiempototal = sum;
    }
}
