import { useEffect, useState } from "react";
import MapContainer, {
	Marker,
	Popup,
	NavigationControl,
	Source,
	Layer
} from "react-map-gl";
import "maplibre-gl/dist/maplibre-gl.css";
import Papa from "papaparse";

const MapaPeru = () => {
	const [viewport, setViewport] = useState({
		latitude: -9.19, // Centrar en Perú
		longitude: -75.0152,
		zoom: 4.8
	});

	const [puntos, setPuntos] = useState([]);
	const [tramos, setTramosList] = useState([]);
	const [selectedPunto, setSelectedPunto] = useState(null);
	const [coordinatesMap, setCoordinatesMap] = useState({});

	const cargarCSV = (file, setDataCallback) => {
		Papa.parse(file, {
			header: true,
			download: true,
			complete: (result) => {
				setDataCallback(result.data);

				// Si estamos cargando los puntos, crear el mapa de coordenadas por `id`
				if (setDataCallback === setPuntos) {
					const coordsMap = {};
					result.data.forEach((punto) => {
						coordsMap[punto.id] = [
							parseFloat(punto.lng),
							parseFloat(punto.lat)
						];
					});
					setCoordinatesMap(coordsMap);
				}
			}
		});
	};

	useEffect(() => {
		cargarCSV("/src/assets/data/oficinas.csv", setPuntos); // Cargar puntos
		cargarCSV("/src/assets/data/tramos.csv", setTramosList); // Cargar tramos
	}, []);

	// Crear un objeto GeoJSON con todas las líneas
	const generateTramosGeoJSON = () => {
		const features = tramos
			.map((tramo) => {
				const startCoords = coordinatesMap[tramo.origen];
				const endCoords = coordinatesMap[tramo.destino];

				if (startCoords && endCoords) {
					return {
						type: "Feature",
						geometry: {
							type: "LineString",
							coordinates: [startCoords, endCoords]
						}
					};
				}
				return null;
			})
			.filter(Boolean);

		return {
			type: "FeatureCollection",
			features: features
		};
	};

	const peruBounds = [
		[-87, -20], // Southwest coordinates
		[-68, 1] // Northeast coordinates
	];

	return (
		<div className="flex justify-center items-center bg-gray-100">
			<MapContainer
				initialViewState={viewport}
				style={{ width: "100%", height: "850px" }}
				mapStyle="https://api.maptiler.com/maps/f6aba462-27c5-4fdf-b246-d75b229628b3/style.json?key=GxDg5CQ3nGa8uKqYsdd9" // URL de tu estilo de MapTiler
				mapLib={import("maplibre-gl")}
				minZoom={4.8}
				maxZoom={10}
				maxBounds={peruBounds}
				onMove={(evt) =>
					setViewport({
						latitude: evt.viewState.latitude,
						longitude: evt.viewState.longitude,
						zoom: evt.viewState.zoom
					})
				}
			>
				{/* Iterar sobre los puntos para agregar los marcadores */}
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
							<div className="bg-pink-500 w-5 h-5 rounded-full opacity-70 border-2" />
						</Marker>
					);
				})}

				{/* Un solo Source para todos los tramos */}
				<Source id="line-source" type="geojson" data={generateTramosGeoJSON()}>
					<Layer
						id="line-layer"
						type="line"
						paint={{
							"line-color": "#0000FF",
							"line-width": 2
						}}
					/>
				</Source>

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
				<NavigationControl position="top-right" />
			</MapContainer>
		</div>
	);
};

export default MapaPeru;
