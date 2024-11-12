import {useEffect, useRef, useState} from "react";
import MapContainer, {Marker, Popup, NavigationControl, Source, Layer} from "react-map-gl";
import "maplibre-gl/dist/maplibre-gl.css";
import Papa from "papaparse";
import camionIcon from "/src/assets/camion.png"; // Icono para los camiones
import rutaData from "/src/assets/data/Data.json"; // JSON para los camiones y rutas
import oficinaIcon from "/src/assets/oficina.png"; // Icono para las oficinas
import ControlesSimulacion from "./ControlesSimulacion"; // Importamos el componente de controles

const MapaPeruSimulacion = ({onUpdateStats}) => {
    //<editor-fold desc="Mapa inicial y su informacion">
    const [viewport, setViewport] = useState({
        latitude: -9.19, // Centrar en Perú
        longitude: -75.0152,
        zoom: 5
    });

    const cargarCSV = (file) => {
        Papa.parse(file, {
            header: true,
            download: true,
            complete: (result) => {
                setPuntos(result.data);
            }
        });
    };

    const peruBounds = [
        [-90, -20], // Southwest coordinates
        [-68, 1] // Northeast coordinates
    ];

    const [puntos, setPuntos] = useState([]); // Para almacenar las oficinas del CSV
    const [selectedPunto, setSelectedPunto] = useState(null); // Para manejar el popup de las oficinas
    const [rutas, setRutas] = useState([]); // Cambiado a "rutas" para almacenar rutas y camiones del JSON
    const [camiones, setCamiones] = useState([]);
    const [currentPositions, setCurrentPositions] = useState({}); // Posiciones actuales de los camiones
    const [selectedCamion, setSelectedCamion] = useState(null); // Para manejar qué camión está seleccionado

    // Referencias para guardar el estado de cada camión
    const tramoIndexRef = useRef([]);
    const progresoTramoRef = useRef([]);
    //</editor-fold>

    useEffect(() => {
        cargarCSV("/src/assets/data/oficinas.csv");
        setRutas(rutaData);

        tramoIndexRef.current = rutaData.map(() => 0);
        progresoTramoRef.current = rutaData.map(() => 0.01);

        return () => clearInterval(intervalRef.current);
    }, []);

    //<editor-fold desc="Movimiento de camiones">
    const calcularTiempoTramo = (distancia, velocidadTramo) => {
        const tiempoTramo = (distancia / velocidadTramo) * 1000;
        return tiempoTramo / velocidad;
    };

	// Función para interpolar entre dos puntos (origen y destino) con un factor de progresión
	const interpolarPosicion = (origen, destino, factor) => {
		const latitud = origen.latitud + (destino.latitud - origen.latitud) * factor;
		const longitud = origen.longitud + (destino.longitud - origen.longitud) * factor;
		return { latitud, longitud };
	};

	const moverCamiones = () => {
		camiones.forEach((camion, camionIndex) => {
			const tramoIndex = tramoIndexRef.current[camionIndex];
			const tramoActual = camion.tramos[tramoIndex];

            if (!tramoActual) return;

			const { distancia, velocidad: velocidadTramo, origen, destino } = tramoActual;
			const tiempoTramo = calcularTiempoTramo(distancia, velocidadTramo);

			const nuevaPosicion = interpolarPosicion(origen, destino, progresoTramoRef.current[camionIndex]);
			setCurrentPositions((prev) => ({
				...prev,
				[camion.camion.codigo]: nuevaPosicion,
			}));

			progresoTramoRef.current[camionIndex] += 0.01 * velocidad;
			if (progresoTramoRef.current[camionIndex] >= 1) {
				tramoIndexRef.current[camionIndex]++;
				progresoTramoRef.current[camionIndex] = 0;

				if (tramoIndexRef.current[camionIndex] >= camion.tramos.length) {
					tramoIndexRef.current[camionIndex] = 0;
				}
			}
		});
	};

    const [simulacionActiva, setSimulacionActiva] = useState(false);
    const [simulacionIniciada, setSimulacionIniciada] = useState(false);
    const [velocidad, setVelocidad] = useState(1); // Multiplicador de velocidad
    const intervalRef = useRef(null);

    const iniciarSimulacion = () => {
        setSimulacionActiva(true);
        setSimulacionIniciada(true);
        iniciarSimulacionInterval();
    }

    const iniciarSimulacionInterval = () => {
        if (intervalRef.current) clearInterval(intervalRef.current);
        intervalRef.current = setInterval(moverCamiones, 1000 / velocidad);
    };

    const pausarSimulacion = () => {
        setSimulacionActiva(false);
        clearInterval(intervalRef.current)
    };

    const reanudarSimulacion = () => {
        setSimulacionActiva(true);
        iniciarSimulacionInterval()
    };

    const pararSimulacion = () => {
        setSimulacionIniciada(false);
        setSimulacionActiva(false);
        clearInterval(intervalRef.current);
        resetearSimulacion();
    };

    const resetearSimulacion = () => {
        tramoIndexRef.current = rutas.map(() => 0);
        progresoTramoRef.current = rutas.map(() => 0);
        setCurrentPositions({})
    }

    const acelerarSimulacion = () => {
        setVelocidad((prev) => Math.min(prev * 2, 16));
    };

    const reducirSimulacion = () => {
        setVelocidad((prev) => Math.max(prev / 2, 0.25));
    };

    //</editor-fold>

    return (
        <div className="flex flex-col justify-center items-center bg-gray-100">
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

            <MapContainer
                initialViewState={viewport}
                style={{width: "100%", height: "850px"}}
                mapStyle="https://api.maptiler.com/maps/f6aba462-27c5-4fdf-b246-d75b229628b3/style.json?key=GxDg5CQ3nGa8uKqYsdd9"
                mapLib={import("maplibre-gl")}
                minZoom={1}
                maxZoom={8}
                maxBounds={peruBounds}
                onMove={(evt) =>
                    setViewport({
                        latitude: evt.viewState.latitude,
                        longitude: evt.viewState.longitude,
                        zoom: evt.viewState.zoom
                    })
                }
            >
                {puntos.map((punto, index) => {
                    const lat = Number.parseFloat(punto.lat);
                    const lng = Number.parseFloat(punto.lng);

                    return (
                        <Marker
                            key={index}
                            latitude={lat}
                            longitude={lng}
                            onClick={(e) => {
                                e.originalEvent.stopPropagation();
                                setSelectedPunto(punto);
                            }}
                        >
                            <img
                                src={oficinaIcon}
                                alt={`Oficina ${punto.departamento}`}
                                style={{width: "20px", height: "20px"}}
                            />
                        </Marker>
                    );
                })}

                {simulacionIniciada && rutas.map((ruta, rutaIndex) => {
                    const {codigo} = ruta.camion; // Usar "codigo" directamente
                    return (
                        <div key={codigo}>
                            {currentPositions[codigo] && (
                                <Marker
                                    latitude={currentPositions[codigo].latitud}
                                    longitude={currentPositions[codigo].longitud}
                                    anchor="center"
                                    onClick={() => setSelectedCamion(codigo)}
                                >
                                    <img
                                        src={camionIcon}
                                        alt={`Camión ${codigo}`}
                                        style={{width: "24px", height: "24px"}}
                                    />
                                </Marker>
                            )}
                            <Source
                                id={`ruta-punteada-${codigo}`}
                                type="geojson"
                                data={{
                                    type: "Feature",
                                    geometry: {
                                        type: "LineString",
                                        coordinates: ruta.tramos.map((tramo) => [
                                            tramo.origen.longitud,
                                            tramo.origen.latitud
                                        ])
                                    }
                                }}
                            >
                                <Layer
                                    id={`route-dotted-${codigo}`}
                                    type="line"
                                    paint={{
                                        "line-color": "#555555",
                                        "line-width": 2,
                                        "line-dasharray": [1, 2]
                                    }}
                                />
                            </Source>

                            <Source
                                id={`ruta-progresiva-${codigo}`}
                                type="geojson"
                                data={{
                                    type: "Feature",
                                    geometry: {
                                        type: "LineString",
                                        coordinates: ruta.tramos.slice(0, tramoIndexRef.current[rutaIndex] + 1)
                                            .map((tramo, idx) => {
                                                if (idx === tramoIndexRef.current[rutaIndex]) {
                                                    const progreso = progresoTramoRef.current[rutaIndex];
                                                    return [
                                                        tramo.origen.longitud + progreso * (tramo.destino.longitud - tramo.origen.longitud),
                                                        tramo.origen.latitud + progreso * (tramo.destino.latitud - tramo.origen.latitud)
                                                    ];
                                                }
                                                return [tramo.destino.longitud, tramo.destino.latitud];
                                            })
                                    }
                                }}
                            >
                                <Layer
                                    id={`route-progress-${codigo}`}
                                    type="line"
                                    paint={{
                                        "line-color": "#FF0000",
                                        "line-width": 2
                                    }}
                                />
                            </Source>
                        </div>
                    );
                })}
                <NavigationControl position="top-right"/>
            </MapContainer>
        </div>
    );
};

export default MapaPeruSimulacion;