package Clases;

import Clases.Oficina;
import Clases.Velocidad;
import Utils.LeerDatos;
import Utils.FuncionesTramo;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Tramo {
    private String ubigeoOrigen;
    private String ubigeoDestino;
    private static Map<String, Double> mapaDistancias = new HashMap<>();
    private Double distanciaTramo;
    private static Map<String, Double> mapaVelocidades = new HashMap<>();
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

    // public void setVelocidadTramo(String ubigeoOrigen, String ubigeoDestino) throws IOException {
    //     List<Oficina> oficinas = LeerDatos.leerOficinas("archivos/oficinas.txt");
    //     Oficina ofi = new Oficina();

    //     String regionOrigen =ofi.retornaRegion(oficinas,ubigeoOrigen);
    //     String regionDestino =ofi.retornaRegion(oficinas,ubigeoDestino);

    //     FuncionesTramo funcTramo = new FuncionesTramo();
    //     List<Velocidad> velocidades = LeerDatos.leerVelocidades("archivos/velocidades.txt");
    //     this.velocidadTramo = funcTramo.calcularVelocidad(regionOrigen,regionDestino,velocidades);
    // }
    public void setVelocidadTramo(String ubigeoOrigen, String ubigeoDestino) throws IOException {
        String clave = ubigeoOrigen + "-" + ubigeoDestino;
        if (!mapaVelocidades.containsKey(clave)) {
            List<Oficina> oficinas = LeerDatos.leerOficinas("archivos/oficinas.txt");
            Oficina ofi = new Oficina();
    
            String regionOrigen = ofi.retornaRegion(oficinas, ubigeoOrigen);
            String regionDestino = ofi.retornaRegion(oficinas, ubigeoDestino);
    
            FuncionesTramo funcTramo = new FuncionesTramo();
            List<Velocidad> velocidades = LeerDatos.leerVelocidades("archivos/velocidades.txt");
            Double velocidad = funcTramo.calcularVelocidad(regionOrigen, regionDestino, velocidades);
            
            // Almacenar en el mapa para acceso futuro
            mapaVelocidades.put(clave, velocidad);
            this.velocidadTramo = velocidad;
        } else {
            this.velocidadTramo = mapaVelocidades.get(clave); // Obtener del mapa si ya está calculado
        }
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

    // public void setDistanciaTramo(String ubigeoOrigen, String ubigeoDestino) throws IOException {
    //     List<Oficina> oficinas = LeerDatos.leerOficinas("archivos/oficinas.txt");
    //     Oficina ofi = new Oficina();
    //     double latitud1 = ofi.retornaLatitud(oficinas,ubigeoOrigen);
    //     double longitud1 = ofi.retornaLongitud(oficinas,ubigeoOrigen);
    //     double latitud2 = ofi.retornaLatitud(oficinas,ubigeoDestino);
    //     double longitud2 = ofi.retornaLongitud(oficinas,ubigeoDestino);
    //     FuncionesTramo funcTramo = new FuncionesTramo();
    //     this.distanciaTramo = funcTramo.calcularDistancia(latitud1,longitud1,latitud2,longitud2);
    // }
    public void setDistanciaTramo(String ubigeoOrigen, String ubigeoDestino) throws IOException {
        String clave = ubigeoOrigen + "-" + ubigeoDestino;
        if (!mapaDistancias.containsKey(clave)) {
            List<Oficina> oficinas = LeerDatos.leerOficinas("archivos/oficinas.txt");
            Oficina ofi = new Oficina();
            double latitud1 = ofi.retornaLatitud(oficinas, ubigeoOrigen);
            double longitud1 = ofi.retornaLongitud(oficinas, ubigeoOrigen);
            double latitud2 = ofi.retornaLatitud(oficinas, ubigeoDestino);
            double longitud2 = ofi.retornaLongitud(oficinas, ubigeoDestino);
    
            FuncionesTramo funcTramo = new FuncionesTramo();
            Double distancia = funcTramo.calcularDistancia(latitud1, longitud1, latitud2, longitud2);
            
            // Almacenar la distancia en el mapa
            mapaDistancias.put(clave, distancia);
            this.distanciaTramo = distancia;
        } else {
            this.distanciaTramo = mapaDistancias.get(clave); // Obtener del mapa
        }
    }


    @Override
    public String toString() {
        return "Tramo{" +
                "ubigeoOrigen='" + ubigeoOrigen + '\'' +
                ", ubigeoDestino='" + ubigeoDestino + '\'' +
                '}';
    }


}