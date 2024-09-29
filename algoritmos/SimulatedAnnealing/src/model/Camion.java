package model;

import java.util.ArrayList;
import java.util.List;

public class Camion {
    private String codigo;
    private char tipo;
    private int capacidad;
    private int cargaActual;
    private List<Paquete> paquetes;
    private Oficina posicionActual;

    public Camion(String codigo, char tipo, Oficina posicionActual) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.paquetes = new ArrayList<>();

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
        this.posicionActual = posicionActual;
    }

    public static List<Camion> inicializarCamiones(Oficina lima, Oficina trujillo, Oficina arequipa) {
        List<Camion> camiones = new ArrayList<>();

        //Tipo A
        for (int i = 0; i < 4; i++)
            camiones.add(new Camion(String.format("A%03d", i + 1), 'A', lima));
        camiones.add(new Camion("A005", 'A', trujillo));
        camiones.add(new Camion("A006", 'A', arequipa));

        //Tipo B
        for (int i = 0; i < 7; i++)
            camiones.add(new Camion(String.format("B%03d", i + 1), 'B', lima));
        for (int i = 7; i < 10; i++)
            camiones.add(new Camion(String.format("B%03d", i + 1), 'B', trujillo));
        for (int i = 10; i < 15; i++)
            camiones.add(new Camion(String.format("B%03d", i + 1), 'B', arequipa));

        //Tipo C
        for (int i = 0; i < 10; i++)
            camiones.add(new Camion(String.format("C%03d", i + 1), 'C', lima));
        for (int i = 10; i < 16; i++)
            camiones.add(new Camion(String.format("C%03d", i + 1), 'C', trujillo));
        for (int i = 16; i < 24; i++)
            camiones.add(new Camion(String.format("B%03d", i + 1), 'C', arequipa));

        int cap = 0;
        for (Camion c : camiones) {
            cap += c.getCapacidad();
        }
        System.out.println("Capacidad total: " + cap + "\n");
        return camiones;
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

    public List<Paquete> getPaquetes() {
        return paquetes;
    }

    public void setPaquetes(List<Paquete> paquetes) {
        this.paquetes = paquetes;
    }

    public Oficina getPosicionActual() {
        return posicionActual;
    }
    //Imprimir ventas
    public void imprimirPaquetes() {
        for (Paquete p : paquetes) {
            System.out.println(p);
        }
    }

    public void agregarPaquete(Paquete paquete) {
        this.paquetes.add(paquete);
    }

    @Override
    public String toString() {
        return "Camion [Código: " + codigo +
                ", Tipo: " + tipo +
                ", Capacidad: " + capacidad +
                ", Carga Actual: " + cargaActual +
                ", Paquetes asignadas: " + paquetes.size() + "]";
    }
}
