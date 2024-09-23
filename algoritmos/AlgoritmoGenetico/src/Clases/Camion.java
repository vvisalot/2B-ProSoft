package Clases;

// Nombre: Angela Llontop
// CódigoPUCP: 20181897 

public class Camion {
    private char tipo;
    private String idCamion;
    private double capacidad;
    private String ubigeo;
    private double carga;
    
    public Camion(String idCamion,double capacidad, String ubigeo) {
        this.tipo = idCamion.charAt(0);
        this.idCamion = idCamion;
        this.capacidad = capacidad;
        this.ubigeo = ubigeo;
        this.carga = 0.0;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public String getIdCamion() {
        return idCamion;
    }

    public void setIdCamion(String idCamion) {
        this.idCamion = idCamion;
    }

    public double getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(double capacidad) {
        this.capacidad = capacidad;
    }

    public double getCarga() {
        return carga;
    }

    public void setCarga(double carga) {
        this.carga = carga;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

 
}

