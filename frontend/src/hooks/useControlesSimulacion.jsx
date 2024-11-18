import {useRef, useState} from "react";
import {getSimulacion} from "../service/simulacion.js";

export const useControlesSimulacion = (rutas, moverCamiones, resetearSimulacion) => {
    const [simulacionActiva, setSimulacionActiva] = useState(false);
    const [simulacionIniciada, setSimulacionIniciada] = useState(false);
    const [simulacionTerminada, setSimulacionTerminada] = useState(false);
    const [velocidad, setVelocidad] = useState(1); // Multiplicador de velocidad

    const [resetRequerido, setResetRequerido] = useState(false); // Nuevo estado

    const intervalRef = useRef(null);

    const moverCamionesVelocidad = () => {
        console.log("Mover camiones con velocidad", velocidad);
        moverCamiones(velocidad, detenerSimulacion)
    }

    const fetchSimulacion = async () => {
        try {
            const response = await getSimulacion();
            console.log(response.data);
            //TODO: Reemplazar esto por un setRutas.Validar que se tenga que anexar las rutas, no reemplazar las que se tengan.
        } catch (error) {
            console.error('Error fetching simulacion:', error);
        }
    };

    const iniciarSimulacion = () => {
        fetchSimulacion();
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
        if (intervalRef.current) clearInterval(intervalRef.current);
        intervalRef.current = setInterval(moverCamionesVelocidad, 1000 / velocidad);
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