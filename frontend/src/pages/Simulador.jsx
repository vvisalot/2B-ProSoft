import { Button, Modal } from "antd";
import Papa from "papaparse";
import { useEffect, useRef, useState } from "react";
import CardLeyenda from "/src/cards/CardLeyenda";
import rutaData from "/src/assets/data/Data.json";
import MapaSimulacion from "/src/components/MapaSimulacion";
import ControlesSimulacion from "../components/ControlesSimulacion.jsx";
import TablaSimulacion from "../components/TablaSimulacion.jsx"; 

const Simulador = () => {
    const almacenesPrincipales = ["150101", "130101", "040101"];
    const [currentTime, setCurrentTime] = useState(new Date().toLocaleTimeString());
    const [numCamiones, setNumCamiones] = useState(0);
    const [numRutas, setNumRutas] = useState(0);
    const [simulacionActiva, setSimulacionActiva] = useState(false);
    const [simulacionIniciada, setSimulacionIniciada] = useState(false);
    const [simulacionTerminada, setSimulacionTerminada] = useState(false);
    const [resetRequerido, setResetRequerido] = useState(false); 
    const [velocidad, setVelocidad] = useState(1); 
    const intervalRef = useRef(null);

    const [rutas, setRutas] = useState(rutaData);
    const [puntos, setPuntos] = useState([]);

    const tramoIndexRef = useRef([]);
    const progresoTramoRef = useRef([]);
    const [currentPositions, setCurrentPositions] = useState({});

	const [tiempoSimulacion, setTiempoSimulacion] = useState(0); // Tiempo simulado acelerado (en segundos)
	const [tiempoReal, setTiempoReal] = useState(0); // Tiempo real transcurrido (en segundos)
	const [realElapsedTime, setRealElapsedTime] = useState(0); // Tiempo real en segundos
    const [simElapsedTime, setSimElapsedTime] = useState(0); // Tiempo simulado en segundos


    useEffect(() => {
        const timer = setInterval(() => {
            setCurrentTime(new Date().toLocaleTimeString());
        }, 1000);
        return () => clearInterval(timer);
    }, []);

	// Cronómetro para el tiempo real
    useEffect(() => {
        let realInterval;
        if (simulacionIniciada && !simulacionTerminada) {
            realInterval = setInterval(() => {
                setRealElapsedTime((prev) => prev + 1);
            }, 1000);
        }
        return () => clearInterval(realInterval);
    }, [simulacionIniciada, simulacionTerminada]);

    useEffect(() => {
        handleUpdateStats(rutaData.length, rutaData.reduce((acc, ruta) => acc + ruta.tramos.length, 0));
    }, []);

    const cargarCSV = (file) => {
        Papa.parse(file, {
            header: true,
            download: true,
            complete: (result) => {
                const data = result.data.map((oficina) => ({
                    ...oficina,
                    almacenPrincipal: almacenesPrincipales.includes(oficina.id)
                }));
                setPuntos(data);
            }
        });
    };

    useEffect(() => {
        cargarCSV("/src/assets/data/oficinas.csv");
        rutaData.forEach((ruta) => {
            let fix = { ...ruta.tramos[0] };
            fix.destino = { ...fix.origen };
            fix.distancia = 0;
            fix.tiempoLlegada = fix.tiempoSalida;
            fix.tiempoEspera = 0;
            ruta.tramos.unshift(fix);
        });
        setRutas(rutaData);
        tramoIndexRef.current = rutaData.map(() => 0);
        progresoTramoRef.current = rutaData.map(() => 0.01);
        return () => clearInterval(intervalRef.current);
    }, []);

    const handleUpdateStats = (camiones, rutas) => {
        setNumCamiones(camiones);
        setNumRutas(rutas);
    };

    const verSimulacionTerminada = () => {
        setSimulacionIniciada(false);
        setSimulacionTerminada(false);
        setResetRequerido(true);
    };

    const iniciarSimulacion = () => {
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
        setSimulacionTerminada(false);
        setResetRequerido(false);
        clearInterval(intervalRef.current);
        resetearSimulacion();
    };

    const detenerSimulacion = () => {
        setSimulacionActiva(false);
        clearInterval(intervalRef.current);
        setSimulacionTerminada(true);
        setResetRequerido(true);
        //alert(`Simulación finalizada. Tiempo total en tiempo real: ${formatTime(realElapsedTime)}`);
		Modal.info({
			title: "Simulación Terminada",
			content: (
				<p>
					La simulación ha terminado. <br />
					Tiempo total de simulación: {formatTime(realElapsedTime)}.
				</p>
			),
		});
	};

    const resetearSimulacion = () => {
		setRealElapsedTime(0); // Reiniciar tiempo real
        setSimElapsedTime(0); // Reiniciar tiempo de simulación
        tramoIndexRef.current = rutas.map(() => 0);
		progresoTramoRef.current = rutas.map(() => 0);
		setTiempoSimulacion(0);
		setTiempoReal(0);
		setCurrentPositions(
			rutas.reduce((acc, ruta) => {
				const { codigo } = ruta.camion;
				acc[codigo] = {
					latitud: ruta.tramos[0].origen.latitud,
					longitud: ruta.tramos[0].origen.longitud
				};
				return acc;
			}, {})
		);
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

    const moverCamiones = () => {
		setSimElapsedTime((prev) => prev + 1); // Incrementar tiempo simulado
        let allFinished = true;

        rutas.forEach((ruta, rutaIndex) => {
            const { codigo } = ruta.camion;
            const tramoIndex = tramoIndexRef.current[rutaIndex];
            const tramoActual = ruta.tramos[tramoIndex];

            if (!tramoActual) return;

            const { distancia, velocidad: velocidadTramo, origen, destino } = tramoActual;
            const tiempoTramo = ((distancia / velocidadTramo) * 1000) / velocidad;
            const progreso = progresoTramoRef.current[rutaIndex];

            const nuevaPosicion = {
                latitud: origen.latitud + (destino.latitud - origen.latitud) * progreso,
                longitud: origen.longitud + (destino.longitud - origen.longitud) * progreso
            };

            setCurrentPositions((prev) => ({ ...prev, [codigo]: nuevaPosicion }));

            progresoTramoRef.current[rutaIndex] += (1 / tiempoTramo) * velocidad;

            if (progresoTramoRef.current[rutaIndex] >= 1) {
                tramoIndexRef.current[rutaIndex]++;
                progresoTramoRef.current[rutaIndex] = 0.01;

                if (tramoIndexRef.current[rutaIndex] >= ruta.tramos.length) {
                    setCurrentPositions((prev) => {
                        const updated = { ...prev };
                        delete updated[codigo];
                        return updated;
                    });
                    tramoIndexRef.current[rutaIndex] = -1;
                } else allFinished = false;
            } else allFinished = false;
        });

        if (allFinished) detenerSimulacion();
    };

	// Función para formatear el tiempo en horas, minutos y segundos
    const formatTime = (totalSeconds) => {
        const hours = Math.floor(totalSeconds / 3600);
        const minutes = Math.floor((totalSeconds % 3600) / 60);
        const seconds = totalSeconds % 60;
        return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    };

    return (
        <div className="h-full flex flex-col md:flex-row p-5 space-y-4 md:space-y-0 md:space-x-4">
            <div className="relative w-full md:w-2/5 lg:w-2/5 mb-4 md:mb-0">
                <h1 className="text-lg font-medium mb-4">Simulación Semanal</h1>
                <CardLeyenda numCamiones={numCamiones} numRutas={numRutas} className="mb-6"/>
                <TablaSimulacion data={rutas} />
            </div>

            <div className="relative w-full md:w-2/3 lg:w-3/4 h-[70vh] md:h-[85vh] border-2 border-gray-300 rounded-lg">
				<div className="absolute top-4 left-4 z-10 bg-white p-2 rounded-lg shadow-lg">
					<h2 className="text-lg font-bold">Tiempo real transcurrido: {formatTime(realElapsedTime)}</h2>
					<h2 className="text-lg font-bold">Tiempo simulado transcurrido: {formatTime(simElapsedTime)}</h2>
				</div>
				<MapaSimulacion
					simulacionActiva={simulacionActiva}
					simulacionIniciada={simulacionIniciada}
					resetRequerido={resetRequerido}
					velocidad={velocidad}
					rutas={rutas}
					puntos={puntos} // Pasamos los puntos al mapa
					currentPositions={currentPositions}
					tramoIndexRef={tramoIndexRef}
					progresoTramoRef={progresoTramoRef}
					onUpdateStats={handleUpdateStats}
				>
				</MapaSimulacion>
				<div className="absolute bottom-4 right-4 z-10 bg-white p-4 rounded-lg shadow-lg">
					<ControlesSimulacion
						simulacionActiva={simulacionActiva}
						simulacionIniciada={simulacionIniciada}
						pausarSimulacion={pausarSimulacion}
						iniciarSimulacion={iniciarSimulacion}
						reanudarSimulacion={reanudarSimulacion}
						pararSimulacion={pararSimulacion}
						acelerarSimulacion={acelerarSimulacion}
						reducirSimulacion={reducirSimulacion}
						velocidad={velocidad}
					/>
				</div>
            </div>
        </div>
    );
};

export default Simulador;
