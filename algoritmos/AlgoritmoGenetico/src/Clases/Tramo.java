package Clases;

import Clases.Oficina;
import Clases.Velocidad;
import Utils.LeerDatos;
import Utils.FuncionesTramo;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

public class Tramo {
    private String ubigeoOrigen;
    private String ubigeoDestino;
    private Double distanciaTramo;
    private Double velocidadTramo;
    private Double horasTramo;

    public Tramo(String ubigeoOrigen, String ubigeoDestino) {
        this.ubigeoOrigen = ubigeoOrigen;
        this.ubigeoDestino = ubigeoDestino;
    }

    // Getters y Setters
    public String getUbigeoOrigen() {
        return ubigeoOrigen;
    }

    public void setUbigeoOrigen(String ubigeoOrigen) {
        this.ubigeoOrigen = ubigeoOrigen;
    }

    public String getUbigeoDestino() {
        return ubigeoDestino;
    }

    public void setUbigeoDestino(String ubigeoDestino) {
        this.ubigeoDestino = ubigeoDestino;
    }

    public void setVelocidadTramo(String ubigeoOrigen, String ubigeoDestino) throws IOException {
        List<Oficina> oficinas = LeerDatos.leerOficinas("archivos/oficinas.txt");
        Oficina ofi = new Oficina();

        String regionOrigen =ofi.retornaRegion(oficinas,ubigeoOrigen);
        String regionDestino =ofi.retornaRegion(oficinas,ubigeoDestino);

        FuncionesTramo funcTramo = new FuncionesTramo();
        List<Velocidad> velocidades = LeerDatos.leerVelocidades("archivos/velocidades.txt");
        this.velocidadTramo = funcTramo.calcularVelocidad(regionOrigen,regionDestino,velocidades);
    }

    public Double getVelocidadTramo() {
        return velocidadTramo;
    }

    // public void setHorasTramo(Double horasTramo){
    //     this.horasTramo = horasTramo;
    // }
    public void setHorasTramo(Double distancia, Double velocidad){
        this.horasTramo = distancia/velocidad;
    }
    public Double getHorasTramo(){
        return horasTramo;
    }

    public Double getDistanciaTramo() {
        return distanciaTramo;
    }

    
    //tramo.setDistanciaTramo(ubigeoOrigen, ubigeoDestino);
    //tramo.setVelocidadTramo(ubigeoOrigen, ubigeoDestino);

    public void setDistanciaTramo(String ubigeoOrigen, String ubigeoDestino) throws IOException {
        List<Oficina> oficinas = LeerDatos.leerOficinas("archivos/oficinas.txt");
        Oficina ofi = new Oficina();
        double latitud1 = ofi.retornaLatitud(oficinas,ubigeoOrigen);
        double longitud1 = ofi.retornaLongitud(oficinas,ubigeoOrigen);
        double latitud2 = ofi.retornaLatitud(oficinas,ubigeoDestino);
        double longitud2 = ofi.retornaLongitud(oficinas,ubigeoDestino);
        FuncionesTramo funcTramo = new FuncionesTramo();
        this.distanciaTramo = funcTramo.calcularDistancia(latitud1,longitud1,latitud2,longitud2);
    }


    @Override
    public String toString() {
        return "Tramo{" +
                "ubigeoOrigen='" + ubigeoOrigen + '\'' +
                ", ubigeoDestino='" + ubigeoDestino + '\'' +
                '}';
    }


}