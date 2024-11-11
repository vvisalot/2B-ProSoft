package model;

import java.util.ArrayList;
import java.util.List;


public class RutaManager {
    private static List<Paquete> paquetesAEntregar = new ArrayList<>();

    public static void agregarPaquete(Paquete paquete){
        paquetesAEntregar.add(paquete);
    }

    public static Paquete obtenerPaquete(int index){
        return paquetesAEntregar.get(index);
    }

    public static int cantidadPaquetes(){
        return paquetesAEntregar.size();
    }

    public static void limpiarPaquetes(){
        paquetesAEntregar = new ArrayList<>();
    }

    public static void agregarPuntoInicial(Paquete paquete){
        paquetesAEntregar.add(0,paquete);
    }
}
