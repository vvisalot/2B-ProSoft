package model;

import java.util.List;

public class Ruta {
    private int id;
    private Camion camion;
    private List<Ubigeo> ubigeosRecorridos; //ESTO LO LLENA EL ALGORITMO
    private List <Venta> ventasRealizadas; // Representan las entregas entre ubigeos
}
