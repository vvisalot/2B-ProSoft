import { useEffect, useState } from "react";
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
	const [intervalId, setIntervalId] = useState(null); // Para manejar el intervalo de simulación
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

	useEffect(() => {
		cargarCSV("/src/assets/data/oficinas.csv"); // Cargar las oficinas del CSV
		setCamiones(rutaData); // Cargar los camiones y rutas desde el JSON
		if (!resetSimulacion) iniciarSimulacion(rutaData); // Iniciar la simulación de los camiones
		return () => clearInterval(intervalId); // Limpiar el intervalo al desmontar el componente
	}, [resetSimulacion]); // Recalcular solo si se reinicia la simulación

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

	// Función para mover los camiones en sus rutas usando la lógica del JSON
	const iniciarSimulacion = (camiones) => {
		let tramoIndexPorCamion = camiones.map(() => 0);
		let progresoTramoPorCamion = camiones.map(() => 0); // Esto es nuevo para guardar el progreso de cada camión

		const moverCamiones = () => {
			camiones.forEach((camion, camionIndex) => {
				const tramoIndex = tramoIndexPorCamion[camionIndex];
				const tramoActual = camion.tramos[tramoIndex];
				if (!tramoActual || !simulacionActiva) return; // Si no hay más tramos o está pausado

				const { distancia, velocidad: velocidadTramo, origen, destino } = tramoActual;
				const tiempoTramo = calcularTiempoTramo(distancia, velocidadTramo);

				// Interpolamos la posición del camión a lo largo del tramo
				const nuevaPosicion = interpolarPosicion(origen, destino, progresoTramoPorCamion[camionIndex]);
				setCurrentPositions((prev) => ({
					...prev,
					[camion.camion.codigo]: nuevaPosicion,
				}));

				// Aumentar el progreso del tramo de forma proporcional a la velocidad
				progresoTramoPorCamion[camionIndex] += 0.01 * velocidad; // Ajustar el incremento con la velocidad
				if (progresoTramoPorCamion[camionIndex] >= 1) {
					// Si se completó el tramo, pasar al siguiente
					tramoIndexPorCamion[camionIndex]++;
					progresoTramoPorCamion[camionIndex] = 0;

					// Si no hay más tramos, reiniciar o detener
					if (tramoIndexPorCamion[camionIndex] >= camion.tramos.length) {
						tramoIndexPorCamion[camionIndex] = 0;
					}
				}
			});
		};

		// Reiniciar el intervalo si ya existe uno
		if (intervalId) clearInterval(intervalId);

		// Establecer el intervalo para mover los camiones continuamente
		const newIntervalId = setInterval(moverCamiones, 50);
		setIntervalId(newIntervalId);
	};

	// Función para pausar la simulación (limpia el intervalo activo)
	const pausarSimulacion = () => {
		clearInterval(intervalId);
		setSimulacionActiva(false); // Cambiar el estado de simulación
	};

	// Función para reanudar la simulación
	const reanudarSimulacion = () => {
		setSimulacionActiva(true);
	};

	// Función para reiniciar la simulación
	const reiniciarSimulacion = () => {
		setResetSimulacion(true); // Forzamos el reinicio
		setCurrentPositions({}); // Limpiamos las posiciones
		setTimeout(() => setResetSimulacion(false), 100); // Reiniciar la simulación después de limpiar
	};

	// Función para acelerar la simulación
	const acelerarSimulacion = () => {
		setVelocidad((prev) => Math.min(prev * 2, 16)); // Incrementar velocidad hasta un máximo de 16x
	};

	// Función para retroceder la simulación (reducir velocidad)
	const reducirSimulacion = () => {
		setVelocidad((prev) => Math.max(prev / 2, 0.25)); // Reducir velocidad hasta un mínimo de 0.25x
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