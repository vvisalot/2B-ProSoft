package Clases;

public class Cromosoma {
    private String idCamion; //solo necesario id (contiene tipo y con eso se ve capacidad)
    private String[] ruta; //el plan de transporte?
    private double fitness;
    
    public Cromosoma(String idCamion, String[] rutaIndices) {
        this.idCamion = idCamion;
        this.ruta = rutaIndices;
    }

    public String getIdCamion() {
        return idCamion;
    }

    public void setIdCamion(String idCamion) {
        this.idCamion = idCamion;
    }

    public String[] getRuta() {
        return ruta;
    }

    public void setRuta(String[] ruta) {
        this.ruta = ruta;
    }
}
