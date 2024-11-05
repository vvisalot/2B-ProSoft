import { useEffect, useRef, useState } from "react";
import MapContainer, { Marker, Popup, NavigationControl, Source, Layer } from "react-map-gl";
import "maplibre-gl/dist/maplibre-gl.css";
import Papa from "papaparse";
import camionIcon from "/src/assets/camion.png"; // Icono para los camiones
import rutaData from "/src/assets/data/Data.json"; // JSON para los camiones y rutas
import oficinaIcon from "/src/assets/oficina.png"; // Icono para las oficinas
import ControlesSimulacion from "./ControlesSimulacion"; // Importamos el componente de controles

const MapaPeruSimulacion = () => {
	const [viewport, setViewport] = useState({
		latitude: -9.19, // Centrar en Perú
		longitude: -75.0152,
		zoom: 5
	});

	const [simulacionActiva, setSimulacionActiva] = useState(true); // Estado de Pausa/Reanudar
	const [velocidad, setVelocidad] = useState(1); // Multiplicador de velocidad
	const intervalRef = useRef(null); // Ref para manejar el intervalo
	const [resetSimulacion, setResetSimulacion] = useState(false); // Para manejar el reinicio de la simulación

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
	const [camiones, setCamiones] = useState([]); // Para almacenar los camiones y rutas del JSON
	const [currentPositions, setCurrentPositions] = useState({}); // Posiciones actuales de los camiones
	const [selectedCamion, setSelectedCamion] = useState(null); // Para manejar qué camión está seleccionado

	// Referencias para guardar el estado de cada camión
	const tramoIndexRef = useRef([]);
	const progresoTramoRef = useRef([]);	

	useEffect(() => {
		cargarCSV("/src/assets/data/oficinas.csv"); // Cargar las oficinas del CSV
		setCamiones(rutaData); // Cargar los camiones y rutas desde el JSON

		// Inicializar referencias de progreso para cada camión
		tramoIndexRef.current = rutaData.map(() => 0);
		progresoTramoRef.current = rutaData.map(() => 0);

	 	iniciarSimulacion(rutaData); // Iniciar la simulación de los camiones
		return () => clearInterval(intervalRef.current); // Limpiar el intervalo al desmontar el componente
	}, []); 

	// Función para calcular el tiempo total que tarda un tramo (en ms) ajustado por la velocidad
	const calcularTiempoTramo = (distancia, velocidadTramo) => {
		const tiempoTramo = (distancia / velocidadTramo) * 1000; // 1 km = 1 segundo
		return tiempoTramo / velocidad; // Ajustar con el multiplicador de velocidad
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

	const iniciarSimulacion = () => {
		if (intervalRef.current) clearInterval(intervalRef.current);
		intervalRef.current = setInterval(moverCamiones, 50);
	};

	useEffect(() => {
		if (simulacionActiva) {
			iniciarSimulacion();
		} else {
			clearInterval(intervalRef.current);
		}
		return () => clearInterval(intervalRef.current);
	}, [simulacionActiva, velocidad]);

	const pausarSimulacion = () => {
		setSimulacionActiva(false);
	};

	const reanudarSimulacion = () => {
		setSimulacionActiva(true);
	};

	const reiniciarSimulacion = () => {
		clearInterval(intervalRef.current);
		tramoIndexRef.current = camiones.map(() => 0);
		progresoTramoRef.current = camiones.map(() => 0);
		setCurrentPositions({});
		setSimulacionActiva(true);
	};

	const acelerarSimulacion = () => {
		setVelocidad((prev) => Math.min(prev * 2, 16));
	};

	const reducirSimulacion = () => {
		setVelocidad((prev) => Math.max(prev / 2, 0.25));
	};

	return (
		<div className="flex flex-col justify-center items-center bg-gray-100">
			{/* Renderizar el componente de controles de simulación */}
			<ControlesSimulacion
				simulacionActiva={simulacionActiva}
				pausarSimulacion={pausarSimulacion}
				reanudarSimulacion={reanudarSimulacion}
				acelerarSimulacion={acelerarSimulacion}
				reducirSimulacion={reducirSimulacion}
				reiniciarSimulacion={reiniciarSimulacion} // Agregar el botón de reinicio
				velocidad={velocidad}
			/>

			<MapContainer
				initialViewState={viewport}
				style={{ width: "100%", height: "850px" }}
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
				{/* Mostrar las oficinas desde el CSV con íconos personalizados */}
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
								src={oficinaIcon} // Usar ícono para las oficinas
								alt={`Oficina ${punto.departamento}`}
								style={{ width: "20px", height: "20px" }}
							/>
						</Marker>
					);
				})}

				{/* Mostrar Popup al hacer clic en una oficina */}
				{selectedPunto && (
					<Popup
						latitude={Number.parseFloat(selectedPunto.lat)}
						longitude={Number.parseFloat(selectedPunto.lng)}
						onClose={() => setSelectedPunto(null)}
						closeOnClick={true}
					>
						<div>
							<p>Departamento: {selectedPunto.departamento}</p>
							<p>Ciudad: {selectedPunto.ciudad}</p>
							<p>Ubigeo: {selectedPunto.ubigeo}</p>
						</div>
					</Popup>
				)}

				{/* Mostrar los camiones y sus rutas desde el JSON */}
				{camiones.map((camion, camionIndex) => (
					<div key={camion.camion.codigo}>
						{/* Mostrar marcador solo en la posición actual del camión */}
						{currentPositions[camion.camion.codigo] && (
							<Marker
								latitude={currentPositions[camion.camion.codigo].latitud}
								longitude={currentPositions[camion.camion.codigo].longitud}
								anchor="center"
								onClick={() => setSelectedCamion(camion.camion.codigo)} // Resaltar el camión al hacer clic
							>
								<img
									src={camionIcon} // Usar ícono para los camiones
									alt={`Camión ${camion.camion.codigo}`}
									style={{ width: "24px", height: "24px" }}
								/>
							</Marker>
						)}

						{/* Mostrar ruta completa del camión como línea */}
						<Source
							id={`ruta-${camion.camion.codigo}`}
							type="geojson"
							data={{
								type: "Feature",
								geometry: {
									type: "LineString",
									coordinates: camion.tramos.map((tramo) => [
										tramo.origen.longitud,
										tramo.origen.latitud
									])
								}
							}}
						>
							<Layer
								id={`route-${camion.camion.codigo}`}
								type="line"
								paint={{
									"line-color": "#FF0000", // Color de la ruta
									"line-width": 4,
									"line-opacity":
										selectedCamion === null || selectedCamion === camion.camion.codigo
											? 1
											: 0.2, // Resaltar la ruta del camión seleccionado y reducir opacidad en las demás
								}}
							/>
						</Source>
					</div>
				))}

				<NavigationControl position="top-right" />
			</MapContainer>
		</div>
	);
};

export default MapaPeruSimulacion;