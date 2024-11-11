import {useEffect, useRef, useState} from "react";
import MapaSimulacion from "/src/components/MapaSimulacion";
import ControlesSimulacion from "../components/ControlesSimulacion.jsx";
import rutasSemanales from "/src/assets/data/Semana.json";
import rutaData from "/src/assets/data/Data.json";
import Papa from "papaparse";
import TablaSimulacion from "../components/TablaSimulacion.jsx"; // Asegúrate de importar Papa Parse
import {Button, Modal} from "antd";

const Simulador = () => {
    const [currentTime, setCurrentTime] = useState(new Date().toLocaleTimeString());
    const [numCamiones, setNumCamiones] = useState(0);
    const [numRutas, setNumRutas] = useState(0);

    const [simulacionActiva, setSimulacionActiva] = useState(false);
    const [simulacionIniciada, setSimulacionIniciada] = useState(false);
    const [simulacionTerminada, setSimulacionTerminada] = useState(false);
    const [velocidad, setVelocidad] = useState(1); // Multiplicador de velocidad
    const intervalRef = useRef(null);

    // Estado para almacenar rutas y puntos de oficinas
    const [rutas, setRutas] = useState();
    const [puntos, setPuntos] = useState([]);

    // Referencias para el estado de cada camión en movimiento
    const tramoIndexRef = useRef([]);
    const progresoTramoRef = useRef([]);
    const [currentPositions, setCurrentPositions] = useState({});

    useEffect(() => {
        // Actualizar la hora cada segundo
        const timer = setInterval(() => {
            setCurrentTime(new Date().toLocaleTimeString());
        }, 1000);
        return () => clearInterval(timer);
    }, []);


    const cargarCSV = (file) => {
        Papa.parse(file, {
            header: true, download: true, complete: (result) => {
                setPuntos(result.data);
            }
        });
    };

    useEffect(() => {
        // Ruta al archivo CSV de oficinas
        cargarCSV("/src/assets/data/oficinas.csv");
        rutaData.forEach((ruta) => {
            let fix = {...ruta.tramos[0]};
            fix.destino = {...fix.origen};
            fix.distancia = 0;
            fix.tiempoLlegada = fix.tiempoSalida;
            fix.tiempoEspera = 0;
            ruta.tramos.unshift(fix);
        });
        setRutas(rutaData);
        tramoIndexRef.current = rutaData.map(() => 0);
        progresoTramoRef.current = rutaData.map(() => 0.01);

        handleUpdateStats(rutaData.length, rutaData.reduce((acc, ruta) => acc + ruta.tramos.length, 0));
        return () => clearInterval(intervalRef.current);
    }, []);


    const handleUpdateStats = (camiones, rutas) => {
        setNumCamiones(camiones);
        setNumRutas(rutas);
    };

    // CONTROLES DE SIMULACIÓN
    const iniciarSimulacion = () => {
        setSimulacionActiva(true);
        setSimulacionIniciada(true);
        iniciarSimulacionInterval();
    };

    const iniciarSimulacionInterval = () => {
        if (intervalRef.current) clearInterval(intervalRef.current);
        intervalRef.current = setInterval(moverCamiones, 1000 / velocidad);
    };

    const pausarSimulacion = () => {
        setSimulacionActiva(false);
        clearInterval(intervalRef.current);
    };

    const reanudarSimulacion = () => {
        setSimulacionActiva(true);
        iniciarSimulacionInterval();
    };

    const pararSimulacion = () => {
        setSimulacionIniciada(false);
        setSimulacionActiva(false);
        clearInterval(intervalRef.current);
        setSimulacionTerminada(true);
    };

    const reiniciarSimulacion = () => {
        setSimulacionTerminada(false);
        setSimulacionIniciada(false);
        setSimulacionActiva(false); // Asegura que no esté activa al reiniciar
        resetearSimulacion();
    }
    const resetearSimulacion = () => {
        tramoIndexRef.current = rutas.map(() => 0);
        progresoTramoRef.current = rutas.map(() => 0);
        setCurrentPositions(rutas.reduce((acc, ruta) => {
            const {codigo} = ruta.camion;
            acc[codigo] = {
                latitud: ruta.tramos[0].origen.latitud, longitud: ruta.tramos[0].origen.longitud,
            };
            return acc;
        }, {}));
    };


    const acelerarSimulacion = () => {
        setVelocidad((prev) => Math.min(prev * 2, 16));
    };

    const reducirSimulacion = () => {
        setVelocidad((prev) => Math.max(prev / 2, 0.25));
    };

    const [camionesActivos, setCamionesActivos] = useState({})

    useEffect(() => {
        // Inicializar camiones activos al cargar rutas
        setCamionesActivos(rutaData.reduce((acc, ruta) => {
            const {codigo} = ruta.camion;
            acc[codigo] = true; // Todos los camiones inician como activos
            return acc;
        }, {}));
    }, [rutas]);

    const cerrarModal = () => {
        setSimulacionTerminada(false);
    };


    // Modificación en moverCamiones para verificar si todos los camiones han terminado
    const moverCamiones = () => {
        rutas.forEach((ruta, rutaIndex) => {
            const {codigo} = ruta.camion;

            if (!camionesActivos[codigo]) return;

            const tramoIndex = tramoIndexRef.current[rutaIndex];
            const tramoActual = ruta.tramos[tramoIndex];

            if (!tramoActual) return;

            const {distancia, velocidad: velocidadTramo, origen, destino} = tramoActual;
            const tiempoTramo = (distancia / velocidadTramo) * 1000 / velocidad;
            const progreso = progresoTramoRef.current[rutaIndex];

            const nuevaPosicion = {
                latitud: origen.latitud + (destino.latitud - origen.latitud) * progreso,
                longitud: origen.longitud + (destino.longitud - origen.longitud) * progreso,
            };

            setCurrentPositions((prev) => ({
                ...prev,
                [codigo]: nuevaPosicion,
            }));

            progresoTramoRef.current[rutaIndex] += (1 / tiempoTramo) * velocidad;

            if (progresoTramoRef.current[rutaIndex] >= 1) {
                tramoIndexRef.current[rutaIndex]++;
                progresoTramoRef.current[rutaIndex] = 0.01;

                if (tramoIndexRef.current[rutaIndex] >= ruta.tramos.length) {
                    // Marcar el camión como inactivo y removerlo de las posiciones
                    setCamionesActivos((prev) => ({
                        ...prev,
                        [codigo]: false,
                    }));
                    setCurrentPositions((prev) => {
                        const updated = {...prev};
                        delete updated[codigo];
                        return updated;
                    });

                    // Verificar si todos los camiones han finalizado su recorrido
                    const allFinished = Object.values(camionesActivos).every((activo) => !activo);
                    if (allFinished) {
                        pararSimulacion(); // Finalizar simulación cuando todos los camiones hayan terminado
                        setSimulacionTerminada(true); // Mostrar mensaje de simulación terminada
                    }
                }
            }
        });
    };
    return (
        <div className="h-fit flex p-2">
            <div className="w-2/6">
                {/*<TablaSimulacion data={rutas}/>*/}
                <button>
                    <span>BOTONCITO</span>
                </button>
            </div>

            <div className="relative w-4/6 h-[92vh] m-auto border border-gray-300 shadow-lg rounded-lg">
                <MapaSimulacion
                    simulacionActiva={simulacionActiva}
                    simulacionIniciada={simulacionIniciada}
                    velocidad={velocidad}
                    rutas={rutas}
                    puntos={puntos} // Pasamos los puntos al mapa
                    currentPositions={currentPositions}
                    tramoIndexRef={tramoIndexRef}
                    progresoTramoRef={progresoTramoRef}
                    onUpdateStats={handleUpdateStats}
                />
                <div className="absolute bottom-4 right-4 z-10 bg-white bg-opacity-80 p-4 rounded-lg shadow-lg">
                    <ControlesSimulacion
                        simulacionActiva={simulacionActiva}
                        simulacionIniciada={simulacionIniciada}
                        pausarSimulacion={pausarSimulacion}
                        iniciarSimulacion={iniciarSimulacion}
                        reanudarSimulacion={reanudarSimulacion}
                        pararSimulacion={pararSimulacion}
                        reiniciarSimulacion={reiniciarSimulacion} // Pasar función de reinicio
                        acelerarSimulacion={acelerarSimulacion}
                        reducirSimulacion={reducirSimulacion}
                        velocidad={velocidad}
                    />
                </div>
            </div>

            {/*/!* Modal de simulación terminada *!/*/}
            {/*<Modal*/}
            {/*    title="SIMULACIÓN TERMINADA"*/}
            {/*    open={simulacionTerminada}*/}
            {/*    onCancel={cerrarModal} // Permite cerrar el modal sin reiniciar*/}
            {/*    footer={[*/}
            {/*        <Button key="cerrar" onClick={cerrarModal}>*/}
            {/*            Cerrar*/}
            {/*        </Button>,*/}
            {/*        <Button*/}
            {/*            key="reiniciar"*/}
            {/*            type="primary"*/}
            {/*            onClick={reiniciarSimulacion}*/}
            {/*        >*/}
            {/*            Reiniciar Simulación*/}
            {/*        </Button>,*/}
            {/*    ]}*/}
            {/*>*/}
            {/*    <p>La simulación ha finalizado. Puedes cerrarla o reiniciarla.</p>*/}
            {/*</Modal>*/}

        </div>
    );
};

export default Simulador;