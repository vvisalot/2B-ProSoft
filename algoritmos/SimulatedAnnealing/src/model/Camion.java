package model;

import java.util.ArrayList;
import java.util.List;

public class Camion {
    private String codigo;
    private char tipo;
    private int capacidad;
    private int cargaActual;
    private List<Venta> ventas;

    //Falta posicion actual
    public Camion(String codigo, char tipo) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.ventas = new ArrayList<>();

        switch (tipo) {
            case 'A':
                this.capacidad = 90;
                break;
            case 'B':
                this.capacidad = 45;
                break;
            case 'C':
                this.capacidad = 30;
                break;
        }
        this.cargaActual = 0;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getCargaActual() {
        return cargaActual;
    }

    public void setCargaActual(int cargaActual) {
        this.cargaActual = cargaActual;
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public void agregarVenta(Venta venta) {
        this.ventas.add(venta);
    }

    @Override
    public String toString() {
        return "Camion [Código: " + codigo +
                ", Tipo: " + tipo +
                ", Capacidad: " + capacidad +
                ", Carga Actual: " + cargaActual +
                ", Ventas asignadas: " + ventas.size() + "]";
    }
    public static List<Camion> inicializarCamiones() {
        List<Camion> camiones = new ArrayList<>();
        //Tipo A
        for (int i = 0; i < 4; i++)
            camiones.add(new Camion(String.format("A%03d", i + 1), 'A'));
        camiones.add(new Camion("A005", 'A'));
        camiones.add(new Camion("A006", 'A'));

        //Tipo B
        for (int i = 0; i < 7; i++)
            camiones.add(new Camion(String.format("B%03d", i + 1), 'B'));
        for (int i = 7; i < 10; i++)
            camiones.add(new Camion(String.format("B%03d", i + 1), 'B'));
        for (int i = 10; i < 15; i++)
            camiones.add(new Camion(String.format("B%03d", i + 1), 'B'));

        //Tipo C
        for (int i = 0; i < 10; i++)
            camiones.add(new Camion(String.format("C%03d", i + 1), 'C'));
        for (int i = 10; i < 16; i++)
            camiones.add(new Camion(String.format("C%03d", i + 1), 'C'));
        for (int i = 16; i < 24; i++)
            camiones.add(new Camion(String.format("B%03d", i + 1), 'C'));

        int cap = 0;
        for (Camion c : camiones) {
            cap += c.getCapacidad();
        }
        System.out.println("Capacidad total: " + cap + "\n");
        return camiones;
    }
}
