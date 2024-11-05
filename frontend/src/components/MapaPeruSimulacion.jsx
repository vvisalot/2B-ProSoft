import { useEffect, useState } from "react";
import MapContainer, { Marker, Popup, NavigationControl, Source, Layer } from "react-map-gl";
import "maplibre-gl/dist/maplibre-gl.css";
import Papa from "papaparse";
import camionIcon from "/src/assets/camion.png"; 
import rutaData from "/src/assets/data/Data.json";

const MapaPeruSimulacion = () => {
	const [viewport, setViewport] = useState({
		latitude: -9.19, // Centrar en Perú
		longitude: -75.0152,
		zoom: 5
	});

	const peruBounds = [
		[-87, -20], // Southwest coordinates
		[-68, 1] // Northeast coordinates
	];

	const [camiones, setCamiones] = useState([]);
	const [camionIndex, setCamionIndex] = useState(0); // Índice del camión actual
	const [tramos, setTramos] = useState([]);
	const [tramoIndex, setTramoIndex] = useState(0);
	const [tramosCompletados, setTramosCompletados] = useState(new Set());

	useEffect(() => {
		// Cargar los datos de los camiones al inicio
		const loadedCamiones = rutaData;
		if (loadedCamiones.length > 0) {
			setCamiones(loadedCamiones);
			setTramos(loadedCamiones[0].tramos); // Establecer tramos del primer camión
		}
	}, []);

	useEffect(() => {
		if (tramos.length > 0) {
			const intervalId = setInterval(() => {
				setTramoIndex((prevIndex) => {
					const nextIndex = prevIndex + 1;
					//console.log("Tramo actual:", tramos[nextIndex]); // Verifica el tramo actual
					if (nextIndex < tramos.length) {
						setViewport({
							latitude: tramos[nextIndex].origen.latitud,
							longitude: tramos[nextIndex].origen.longitud,
							zoom: 6
						});
						setTramosCompletados((prev) => {
							const newSet = new Set(prev);
							newSet.add(prevIndex); // Marca el tramo anterior como completado
							//console.log("Tramos completados:", Array.from(newSet)); // Verifica los tramos completados
							return newSet;
						});
						return nextIndex;
					}
							// Si se alcanzó el final de los tramos, pasar al siguiente camión
							setCamionIndex((prevCamionIndex) => {
								const nextCamionIndex = (prevCamionIndex + 1) % camiones.length; // Ciclo a través de los camiones
								setTramos(camiones[nextCamionIndex].tramos); // Establecer tramos del siguiente camión
								setTramoIndex(0); // Reiniciar el índice de tramo
								setTramosCompletados(new Set()); // Reiniciar tramos completados
								return nextCamionIndex;
							});
							clearInterval(intervalId); // Detener la animación al final
							return prevIndex; // Retornar el índice anterior si se detuvo el intervalo
				});
			}, 2000);

			return () => clearInterval(intervalId);
		}
	}, [tramos,camiones]);

	const getLineColor = (index) => {
		return tramosCompletados.has(index) ? "#00ff00" : "#888"; // Verde para completados, gris para no completados
	};

	return (
		<div className="flex justify-center items-center bg-gray-100">
			<MapContainer
				initialViewState={viewport}
				style={{ width: "100%", height: "850px" }}
				mapStyle="https://api.maptiler.com/maps/f6aba462-27c5-4fdf-b246-d75b229628b3/style.json?key=GxDg5CQ3nGa8uKqYsdd9"
				mapLib={import("maplibre-gl")}
				minZoom={4.8}
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
				{/* Mostrar marcador para el tramo actual */}
				{tramos[tramoIndex] && (
					<Marker
						latitude={tramos[tramoIndex].origen.latitud}
						longitude={tramos[tramoIndex].origen.longitud}
						anchor="center"
					>
						<img
							src={camionIcon}
							alt="Camión"
							style={{ width: "24px", height: "24px" }}
						/>
					</Marker>
				)}

				{/* Mostrar ruta completa como línea */}
				<Source
					id="ruta"
					type="geojson"
					data={{
						type: "Feature",
						geometry: {
							type: "LineString",
							coordinates: tramos.map((tramo) => [
								tramo.origen.longitud,
								tramo.origen.latitud
							])
						}
					}}
				>
					<Layer
						id="route"
						type="line"
						paint={{
							"line-color": [
								"case",
								...tramos.flatMap((_, index) => [
									["==", ["get", "index"], index],
									getLineColor(index),
								]),
								"#888" // Color por defecto si no se cumple
							],
							"line-width": 4
						}}
					/>
				</Source>

				<NavigationControl position="top-right" />
			</MapContainer>
		</div>
	);
};

export default MapaPeruSimulacion;
