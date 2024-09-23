package Clases;


public class Cromosoma {
    private String idCamion;
    private String[] ruta;
    
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
