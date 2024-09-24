package Clases;

import Clases.Tramo;
import java.util.List;
public class Cromosoma {
    private String idCamion; //solo necesario id (contiene tipo y con eso se ve capacidad)
    private List<Tramo> ruta; //el plan de transporte?
    private double tiempototal;  //suma de todas las horas de tramos
    
    public Cromosoma(String idCamion) {
        this.idCamion = idCamion;
    }

    public String getIdCamion() {
        return idCamion;
    }

    public void setIdCamion(String idCamion) {
        this.idCamion = idCamion;
    }
}
