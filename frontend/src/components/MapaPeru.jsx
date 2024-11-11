import { useEffect, useState } from "react";
import MapContainer, { Marker, Popup, NavigationControl, Source, Layer } from "react-map-gl";
import "maplibre-gl/dist/maplibre-gl.css";
import Papa from "papaparse";
import camionIcon from "/src/assets/icons/camion.png";
import rutaData from "/src/assets/data/Data.json";

const MapaPeru = () => {
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
		[-87, -20], // Southwest coordinates
		[-68, 1] // Northeast coordinates
	];
	const [puntos, setPuntos] = useState([]);
	const [selectedPunto, setSelectedPunto] = useState(null);

	useEffect(() => {
		cargarCSV("/src/assets/data/oficinas.csv");
	});
	return (
		<div className="flex justify-center items-center bg-gray-100">
			<MapContainer
				initialViewState={viewport}
				style={{ width: "100%", height: "850px" }}
				mapStyle="https://api.maptiler.com/maps/f6aba462-27c5-4fdf-b246-d75b229628b3/style.json?key=GxDg5CQ3nGa8uKqYsdd9" // URL de tu estilo de MapTiler
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
				{/* Iterar sobre los puntos para agregar los marcadores*/ }
				{puntos.map((punto, index) => {
					const lat = Number.parseFloat(punto.lat);
					const lng = Number.parseFloat(punto.lng);

					// console.log(`Punto ID: ${punto.id}, Lat: ${lat}, Lng: ${lng}`);

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
