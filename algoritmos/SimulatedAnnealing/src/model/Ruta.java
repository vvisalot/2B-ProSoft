package model;

import java.util.List;

public class Ruta {
    private int id;
    private Camion camion;
    private List<Oficina> oficinasRecorridas; //ESTO LO LLENA EL ALGORITMO
    private List <Venta> ventasRealizadas; // Representan las entregas entre ubigeos
}
