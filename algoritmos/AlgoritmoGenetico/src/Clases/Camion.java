package Clases;

// Nombre: Angela Llontop
// CódigoPUCP: 20181897 

public class Camion {
    private char tipo; //A, B, C
    private String idCamion; //la placa
    private double capacidad; //dato, de la mano de tipo camion
    private String ubigeo; //
    private double carga; 
    private int cantPedidos; //numero pedidos
    
    public Camion(String idCamion,double capacidad, String ubigeo) {
        this.tipo = idCamion.charAt(0);
        this.idCamion = idCamion;
        this.capacidad = capacidad;
        this.ubigeo = ubigeo;
        this.carga = 0.0;
        this.cantPedidos = 0;
    }

    public Camion() {};

    public Camion(String idCamion){
        this.idCamion = idCamion;
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

    public int getCantPeidos() {
        return cantPedidos;
    }

    public void setCantPedidos(int cantPedidos) {
        this.cantPedidos = cantPedidos;
    }

    public boolean estaEnMantenimiento(List<Mantenimiento> listaMant){
        for(Mantenimiento mant: listaMant){
            if(mant.){
                return true;
            }
        }
        return false;
    }
}

