import {useRef, useState} from "react";
import {actualizarReloj,getSimulacion} from "../service/simulacion.js";

export const useControlesSimulacion = (rutas, setRutas, moverCamiones, resetearSimulacion,currentTime,setCurrentTime) => {
    const [simulacionActiva, setSimulacionActiva] = useState(false);
    const [simulacionIniciada, setSimulacionIniciada] = useState(false);
    const [simulacionTerminada, setSimulacionTerminada] = useState(false);
    const [velocidad, setVelocidad] = useState(1); // Multiplicador de velocidad

    const [resetRequerido, setResetRequerido] = useState(false); // Nuevo estado

    const intervalRef = useRef(null);
    const primeraSimulacionRef = useRef(true);
    const simulatedClockRef = useRef(new Date(currentTime))

    const moverCamionesVelocidad = () => {
        console.log("Mover camiones con velocidad", velocidad);
        moverCamiones(velocidad, detenerSimulacion)
    }
    
    const fetchSimulacion = async () => {
        try {
            // Actualizar el reloj simulado con la fecha inicial
            await actualizarReloj(currentTime);
            // Obtener las soluciones iniciales
            const response = await getSimulacion();
            console.log(response.data)
            //TODO: Reemplazar esto por un setRutas.Validar que se tenga que anexar las rutas, no reemplazar las que se tengan.
        } catch (error) {
            console.error("Error al iniciar la simulación:", error);
        }
    };

    
    const avanzarSimulacion = async () => {
        try {
            let nuevaHora;
            if (primeraSimulacionRef.current) {
                // En la primera simulación, usar la fecha inicial directamente
                nuevaHora = new Date(currentTime);
                primeraSimulacionRef.current = false; // Cambiar referencia
                console.log("Primera simulación, hora inicial:", nuevaHora.toISOString().split(".")[0]);
            } else {
                nuevaHora = new Date(simulatedClockRef.current.getTime() + 6 * 60 * 60 * 1000);
            }
            simulatedClockRef.current = nuevaHora;
            console.log("Avanzando el reloj simulado a:", nuevaHora.toISOString().split(".")[0])

            const fechaFormateada = nuevaHora.toISOString().split(".")[0];
            await actualizarReloj(fechaFormateada);

            // Obtener nuevas soluciones del backend
            const response = await getSimulacion();

            // Anexar nuevas soluciones a las rutas actuales
            setRutas((prevRutas) => {
                const nuevasRutas = response.data;

                // Crear un mapa con las rutas actuales indexadas por el código del camión
                const rutasMap = new Map(prevRutas.map((ruta) => [ruta.camion.codigo, ruta]));

                nuevasRutas.forEach((nuevaRuta) => {
                    const codigoCamion = nuevaRuta.camion.codigo;
                    if (rutasMap.has(codigoCamion)) {
                        // Si el camión ya existe, reemplazamos su ruta completa
                        rutasMap.set(codigoCamion, nuevaRuta);
                    } else {
                        // Si el camión no existe, lo añadimos al mapa
                        rutasMap.set(codigoCamion, nuevaRuta);
                    }
                });

                // Convertimos el mapa de nuevo a un arreglo de rutas
                return Array.from(rutasMap.values());
            });

            moverCamionesVelocidad();
        } catch (error) {
            console.error("Error al avanzar la simulación:", error);
        }
    };

    const iniciarSimulacion = () => {
        console.log("Iniciando simulación");

        if (resetRequerido) {
            pararSimulacion();
            setResetRequerido(false);
        }

        setSimulacionTerminada(false);
        setSimulacionActiva(true);
        setSimulacionIniciada(true);
        iniciarSimulacionInterval();
    };

    const iniciarSimulacionInterval = () => {
        console.log("Configurando intervalo");
        //avanzarSimulacion()
        if (intervalRef.current) clearInterval(intervalRef.current);
        intervalRef.current = setInterval(avanzarSimulacion, 90000); // Cada 1 minuto se llamará al algoritmo
    };

    const pausarSimulacion = () => {
        console.log("Pausar simulación");
        setSimulacionActiva(false);
        clearInterval(intervalRef.current);
    };

    const reanudarSimulacion = () => {
        setSimulacionActiva(true);
        iniciarSimulacionInterval();
    };

    const pararSimulacion = () => {
        console.log("Parar simulación");
        setSimulacionIniciada(false);
        setSimulacionActiva(false);
        setSimulacionTerminada(false);
        setResetRequerido(false);
        clearInterval(intervalRef.current);
        resetearSimulacion();
    };

    const detenerSimulacion = () => {
        console.log("Detener simulación");
        setSimulacionActiva(false);
        clearInterval(intervalRef.current);
        setSimulacionTerminada(true);
        setResetRequerido(true);
    };


    const acelerarSimulacion = () => {
        setVelocidad((prev) => {
            const nuevaVelocidad = Math.min(prev * 2, 16);
            iniciarSimulacionInterval();
            return nuevaVelocidad;
        });
    };

    const reducirSimulacion = () => {
        setVelocidad((prev) => {
            const nuevaVelocidad = Math.max(prev / 2, 0.25);
            iniciarSimulacionInterval();
            return nuevaVelocidad;
        });
    };

    // Función para cerrar el modal y ver la simulación terminada
    const verSimulacionTerminada = () => {
        setSimulacionIniciada(false);
        setSimulacionTerminada(false);
        setResetRequerido(true);
    };

    return {
        simulacionActiva,
        simulacionIniciada,
        simulacionTerminada,
        resetRequerido,
        velocidad,
        iniciarSimulacion,
        pausarSimulacion,
        reanudarSimulacion,
        pararSimulacion,
        detenerSimulacion,
        acelerarSimulacion,
        reducirSimulacion,
        verSimulacionTerminada,
        intervalRef,
    };
}