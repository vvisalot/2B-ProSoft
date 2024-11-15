import MapContainer, {
	Marker,
	NavigationControl,
	Source,
	Layer
} from "react-map-gl";
import "maplibre-gl/dist/maplibre-gl.css";
import camionIcon from "/src/assets/icons/camion.png";
import oficinaIcon from "/src/assets/icons/oficina.png";
import almacenPrincipalIcon from "/src/assets/icons/storage.png";

import {useEffect, useState} from "react";
import Papa from "papaparse";

const MapaSimulacion = ({
	simulacionActiva,
	simulacionIniciada,
	resetRequerido,
	velocidad,
	rutas,
	currentPositions,
	tramoIndexRef,
	progresoTramoRef,
	onUpdateStats
}) => {
	const [viewport, setViewport] = useState({
		latitude: -9.19,
		longitude: -75.0152,
		zoom: 2.5
	});

	const peruBounds = [
		[-90.0, -21.0], // Southwest coordinates, para incluir Tacna y la región sur
		[-65.0, 3.0] // Northeast coordinates, para cubrir el norte y noreste
	];


	const [puntos, setPuntos] = useState([]);
	const almacenesPrincipales = ["150101", "130101", "040101"];

	// Cargar el CSV de oficinas
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
	});

	return (
		<MapContainer
			initialViewState={viewport}
			mapStyle="https://api.maptiler.com/maps/f6aba462-27c5-4fdf-b246-d75b229628b3/style.json?key=GxDg5CQ3nGa8uKqYsdd9"
			mapLib={import("maplibre-gl")}
			maxZoom={8}
			minZoom={2.5}
			maxBounds={peruBounds}
			onMove={(evt) =>
				setViewport({
					latitude: evt.viewState.latitude,
					longitude: evt.viewState.longitude,
					zoom: evt.viewState.zoom
				})
			}
			attributionControl={false}
		>
			{/* Marcadores de oficinas */}
			{puntos.map((punto, index) => (
				<Marker
					key={index}
					latitude={parseFloat(punto.lat)}
					longitude={parseFloat(punto.lng)}
				>
					<img
						src={punto.almacenPrincipal ? almacenPrincipalIcon : oficinaIcon}
						alt={`Oficina ${punto.departamento}`}
						style={{ width: "20px", height: "20px" }}
					/>
				</Marker>
			))}

			{/* Marcadores de camiones */}
			{simulacionIniciada &&
				Object.entries(currentPositions).map(([codigo, posicion]) => (
					<Marker
						key={codigo}
						latitude={posicion.latitud}
						longitude={posicion.longitud}
					>
						<img
							src={camionIcon}
							alt={`Camión ${codigo}`}
							style={{ width: "24px", height: "24px" }}
						/>
					</Marker>
				))}

			{/* Líneas de rutas y progreso de rutas */}
			{(resetRequerido || simulacionIniciada) &&
				rutas.map((ruta, rutaIndex) => (
					<div key={rutaIndex}>
						{/* Línea de ruta completa (dotted) */}
						<Source
							id={`ruta-completa-${rutaIndex}`}
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
								id={`ruta-dotted-${rutaIndex}`}
								type="line"
								paint={{
									"line-color": "#555555",
									"line-width": 2,
									"line-dasharray": [1, 2]
								}}
							/>
						</Source>

						<Source
							id={`ruta-progreso-${rutaIndex}`}
							type="geojson"
							data={{
								type: "Feature",
								geometry: {
									type: "LineString",
									coordinates: ruta.tramos
										.slice(0, tramoIndexRef.current[rutaIndex] + 1)
										.map((tramo, idx) => {
											if (idx === tramoIndexRef.current[rutaIndex]) {
												const progreso = progresoTramoRef.current[rutaIndex];
												return [
													tramo.origen.longitud +
														progreso *
															(tramo.destino.longitud - tramo.origen.longitud),
													tramo.origen.latitud +
														progreso *
															(tramo.destino.latitud - tramo.origen.latitud)
												];
											}
											return [tramo.destino.longitud, tramo.destino.latitud];
										})
								}
							}}
						>
							<Layer
								id={`ruta-progress-${rutaIndex}`}
								type="line"
								paint={{
									"line-color": "#FF0000",
									"line-width": 2
								}}
							/>
						</Source>
					</div>
				))}

			<NavigationControl position="top-right" />
		</MapContainer>
	);
};

export default MapaSimulacion;
