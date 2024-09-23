package Clases;

import java.util.List;

public class Oficina {
    private String ubigeo;          //030101
    private String departamento;    //APURIMAC
    private String provincia;       //Abancay
    private double latitud;         //
    private double longitud;        //
    private String regionNatural;   //SIERRA
    private int almacen;         //Capacidad

    public Oficina(String ubigeo, String departamento, String provincia, double latitud, double longitud, String regionNatural, int almacen) {
        this.ubigeo = ubigeo;
        this.departamento = departamento;
        this.provincia = provincia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.regionNatural = regionNatural;
        this.almacen = almacen;
    }

    public Oficina (){};

    // Getters y Setters
    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getRegionNatural() {
        return regionNatural;
    }

    public void setRegionNatural(String regionNatural) {
        this.regionNatural = regionNatural;
    }

    public int getAlmacen() {
        return almacen;
    }

    public void setAlmacen(int almacen) {
        this.almacen = almacen;
    }

    public double retornaLatitud(List<Oficina> oficinas, String ubigeo){
        for (Oficina oficina : oficinas) {
            if (oficina.getUbigeo().equalsIgnoreCase(ubigeo)) {
                return oficina.latitud;
            }
        }
        return -1;
    }

    public double retornaLongitud(List<Oficina> oficinas, String ubigeo){
        for (Oficina oficina : oficinas) {
            if (oficina.getUbigeo().equalsIgnoreCase(ubigeo)) {
                return oficina.longitud;
            }
        }
        return -1;
    }

    public String retornaRegion(List<Oficina> oficinas,String ubigeo){
        for (Oficina oficina : oficinas) {
            if (oficina.getUbigeo().equalsIgnoreCase(ubigeo)) {
                return oficina.regionNatural;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "Oficina{" +
                "ubigeo='" + ubigeo + '\'' +
                ", departamento='" + departamento + '\'' +
                ", provincia='" + provincia + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", regionNatural='" + regionNatural + '\'' +
                ", almacen='" + almacen + '\'' +
                '}';
    }
}