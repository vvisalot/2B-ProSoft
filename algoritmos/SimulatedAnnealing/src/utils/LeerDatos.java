package utils;

import algorithm.GrafoTramos;
import model.Bloqueo;
import model.Oficina;
import model.Tramo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

public class LeerDatos {
    private static final Logger logger = Logger.getLogger(LeerDatos.class.getName());

    // Método para leer las oficinas desde un archivo y almacenarlas en un HashMap
    public static Map<String, Oficina> leerOficinasDesdeArchivo(String filePath) {
        Map<String, Oficina> mapaOficinas = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(","); // Asume que los datos están separados por comas
                // Asignar los valores a los atributos correspondientes
                String codigo = datos[0];  // UBIGEO
                Oficina oficina = getOficina(datos, codigo);
                // Agregar la oficina al mapa usando el código (ubigeo) como clave
                mapaOficinas.put(codigo, oficina);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapaOficinas;
    }

    private static Oficina getOficina(String[] datos, String codigo) {
        String departamento = datos[1];  // DEP
        String provincia = datos[2];  // PROV
        double latitud = Double.parseDouble(datos[3]);  // LATITUD
        double longitud = Double.parseDouble(datos[4]);  // LONGITUD
        String regionNatural = datos[5];  // REGION.NATURAL
        int capacidad = Integer.parseInt(datos[6]);  // ALMACEN

        // Crear una nueva instancia de Oficina
        return new Oficina(codigo, departamento, provincia, latitud, longitud, regionNatural, capacidad);
    }

    public static Pair<List<Tramo>,Map<String, Set<Tramo>>> leerTramosDesdeArchivo(String filePath, Map<String, Oficina> mapaOficinas) {
        List<Tramo> tramos = new ArrayList<>();
        Map<String, Set<Tramo>> mapaTramos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(" => ");  // Asumimos que están separados por comas

                // Extraemos los valores de ubigeo para origen y destino
                String ubigeoOrigen = datos[0].trim();
                String ubigeoDestino = datos[1].trim();

                // Buscamos las oficinas correspondientes en el mapa
                Oficina oficinaOrigen = mapaOficinas.get(ubigeoOrigen);
                Oficina oficinaDestino = mapaOficinas.get(ubigeoDestino);

                if (oficinaOrigen != null && oficinaDestino != null) {
                    //TODO: Revisar si es necesario agregar la distancia
                    Tramo tramo = new Tramo(oficinaOrigen, oficinaDestino, 0);
                    tramos.add(tramo);
                    if(mapaTramos.containsKey(ubigeoOrigen)){
                        mapaTramos.get(ubigeoOrigen).add(tramo);
                    }else {
                        Set<Tramo> tramosOrigen = new HashSet<>();
                        tramosOrigen.add(tramo);
                        mapaTramos.put(ubigeoOrigen, tramosOrigen);
                    }
                } else {
                    System.err.println("Oficina no encontrada para ubigeo: " + ubigeoOrigen + " o " + ubigeoDestino);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Pair<>(tramos, mapaTramos);
    }


    public static void leerBloqueos(String archivo, GrafoTramos grafoTramos) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(archivo));

        // Formato para "MMdd,HH:mm" (mes, día y hora)
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("MMdd");
        int anioActual = LocalDateTime.now().getYear(); // Usamos el año actual

        for (String linea : lineas) {
            String[] datos = linea.split(";");

            // Parsear las fechas de inicio y fin: "0101,13:32==0119,10:39"
            String[] tiempos = datos[1].split("==");
            String fechaInicioStr = tiempos[0].split(",")[0].trim(); // "0101"
            String horaInicioStr = tiempos[0].split(",")[1].trim(); // "13:32"
            String fechaFinStr = tiempos[1].split(",")[0].trim(); // "0119"
            String horaFinStr = tiempos[1].split(",")[1].trim(); // "10:39"

            // Parsear fecha y hora de inicio
            MonthDay mesDiaInicio = MonthDay.parse(fechaInicioStr, formatoFecha); // Parsear "MMdd"
            LocalTime horaInicio = LocalTime.parse(horaInicioStr, formatoHora); // Parsear "HH:mm"
            LocalDate fechaInicioCompleta = mesDiaInicio.atYear(anioActual); // Añadir el año actual
            LocalDateTime fechaHoraInicio = LocalDateTime.of(fechaInicioCompleta, horaInicio);

            // Parsear fecha y hora de fin
            MonthDay mesDiaFin = MonthDay.parse(fechaFinStr, formatoFecha); // Parsear "MMdd"
            LocalTime horaFin = LocalTime.parse(horaFinStr, formatoHora); // Parsear "HH:mm"
            LocalDate fechaFinCompleta = mesDiaFin.atYear(anioActual); // Añadir el año actual
            LocalDateTime fechaHoraFin = LocalDateTime.of(fechaFinCompleta, horaFin);

            // Crear el objeto Bloqueo y asignarlo al Tramo
            Bloqueo bloqueo = new Bloqueo(fechaHoraInicio, fechaHoraFin);
            // Parsear el tramo: "250301 => 220501"
            String[] tramos = datos[0].split("=>");
            String ubigeoOrigen = tramos[0].trim();
            String ubigeoDestino = tramos[1].trim();

            grafoTramos.agregarBloqueo(bloqueo,ubigeoOrigen,ubigeoDestino);
        }
    }

}