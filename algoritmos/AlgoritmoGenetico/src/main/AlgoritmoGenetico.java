package main;

import Clases.*;
import Utils.LeerDatos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Random;

public class AlgoritmoGenetico {
    static final int TAMANO_POBLACION = 10;
    static final int NUM_GENERACIONES = 10; //antes era 100
    static Random random = new Random();

    public static void main(String[] args) throws IOException {

        // Generar población inicial
            //probar con solamente un pedido
        Venta pedido = new Venta();
        List<Tramo> tramos = LeerDatos.leerTramos("archivos/tramos.txt");
        Camion camion = new Camion();
        ArrayList<Cromosoma> poblacion = generarPoblacionInicial(pedido,tramos,camion);

        // Evolucionar por generaciones
        for (int gen = 0; gen < NUM_GENERACIONES; gen++) {
            poblacion = evolucionarPoblacion(poblacion);
            Cromosoma mejorIndividuo = seleccionarMejor(poblacion);
            System.out.println("Generación " + gen + ": Mejor tiempo = " + mejorIndividuo.getTiempoTotal());
        }
    }

    public static ArrayList<Cromosoma> generarPoblacionInicial(Venta pedido, List<Tramo> tramos, Camion camion) {
        ArrayList<Cromosoma> poblacion = new ArrayList<>();

        //Verificar capacidad del camión para el pedido --> solo un camion
        if (pedido.getCantidad() > camion.getCapacidad()) {
            System.out.println("El camión no tiene capacidad suficiente para este pedido.");
            return poblacion;
        }
        
        // Generar todas las rutas posibles entre el origen y el destino del pedido
        List<List<Tramo>> rutasPosibles = generarRutas(pedido.getUbigeoOrigen(), pedido.getUbigeoDestino(), tramos);

        // Crear cromosomas (individuos) a partir de las rutas
        for (List<Tramo> ruta : rutasPosibles) {
            poblacion.add(new Cromosoma(ruta, camion));
        }

        return poblacion;
    }

    // Función recursiva para generar todas las rutas posibles desde el origen al destino
    public static List<List<Tramo>> generarRutas(String origen, String destino, List<Tramo> tramos) {
        List<List<Tramo>> rutas = new ArrayList<>();

        for (Tramo tramo : tramos) {
            if (tramo.getUbigeoOrigen().equals(origen)) {
                if (tramo.getUbigeoDestino().equals(destino)) {
                    // Ruta directa encontrada
                    List<Tramo> rutaDirecta = new ArrayList<>();
                    rutaDirecta.add(tramo);
                    rutas.add(rutaDirecta);
                } else {
                    // Buscar rutas a través de destinos intermedios
                    List<List<Tramo>> rutasIntermedias = generarRutas(tramo.getUbigeoDestino(), destino, tramos);
                    for (List<Tramo> ruta : rutasIntermedias) {
                        List<Tramo> nuevaRuta = new ArrayList<>();
                        nuevaRuta.add(tramo);
                        nuevaRuta.addAll(ruta);
                        rutas.add(nuevaRuta);
                    }
                }
            }
        }
        return rutas;
    }




    public static ArrayList<Cromosoma> evolucionarPoblacion(ArrayList<Cromosoma> poblacion) {
        ArrayList<Cromosoma> nuevaPoblacion = new ArrayList<>();
        for (int i = 0; i < poblacion.size(); i++) {
            Cromosoma padre1 = seleccionarMejor(poblacion);
            Cromosoma padre2 = seleccionarMejor(poblacion);
            Cromosoma hijo = cruzar(padre1, padre2);
            mutar(hijo);
            nuevaPoblacion.add(hijo);
        }
        return nuevaPoblacion;
    }

    public static Cromosoma cruzar(Cromosoma padre1, Cromosoma padre2) {
        ArrayList<Tramo> nuevaRuta = new ArrayList<>(padre1.getRuta().subList(0, padre1.getRuta().size() / 2)); // Mitad del padre1
        for (Tramo tramo : padre2.getRuta()) {
            if (!nuevaRuta.contains(tramo)) {
                nuevaRuta.add(tramo);           //Mitad de ruta2 sin duplicados
            }
        }
        return new Cromosoma(nuevaRuta, new Camion(padre1.getIdCamion()));
    }

    public static void mutar(Cromosoma individuo) {
        int i = random.nextInt(individuo.getRuta().size());
        int j = random.nextInt(individuo.getRuta().size());
        Collections.swap(individuo.getRuta(), i, j); // Mutar dos ciudades
    }

    public static Cromosoma seleccionarMejor(ArrayList<Cromosoma> poblacion) {
        return poblacion.stream().min((a, b) -> Double.compare(a.getTiempoTotal(), b.getTiempoTotal())).get(); // Mejor fitness
    }

}
