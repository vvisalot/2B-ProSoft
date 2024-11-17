import { Button, DatePicker, Dropdown, Tabs } from "antd";
import Title from "antd/es/skeleton/Title";
import { useEffect, useState } from "react";
import CardLeyenda from "../../cards/CardLeyenda";
import MapaSimulacion from "../../components/Simulador/MapaSimulacion.jsx";
import TablaFlota from "../../components/Simulador/TablaFlota.jsx";
import Colapso from "../Simulador/SimuColapso"
import Semanal from "../Simulador/SimuSemanal"

const SimuColapso = () => {

	const [currentTime, setCurrentTime] = useState(new Date().toLocaleTimeString());
	// Estado para controlar la visibilidad de la sección inferior
	const [isSectionVisible, setIsSectionVisible] = useState(true);

	// Actualizar la hora en tiempo real
	useEffect(() => {
		const timer = setInterval(() => {
	 		setCurrentTime(new Date().toLocaleTimeString());
	 	}, 1000);
		return () => clearInterval(timer); // Limpiar intervalo en desmontaje
	}, []);



	return (
		<div className="h-full flex flex-col relative">
			{/* Contenedor Superior de hora */}
			<div className="w-full flex justify-center items-center" style={{ height: "25px", backgroundColor: '#ffd700' }}>
                <div className="flex justify-between items-center w-full max-w-3xl">
                    <div className="text-lg">
                        <span>Fecha: {new Date().toLocaleDateString()}</span>
                    </div>
                    <div className="text-lg">
                        <span>Hora: {currentTime}</span>
                    </div>
                </div>
            </div>
		
			<div className="flex flex-1">
				{/* Contenedor izquierdo */}
				<div className="p-4 overflow-hidden" style={{ height: "490px", width: "400px", backgroundColor: '#fafafa' }}>
                    <h1 style={{ fontSize: "24px", fontWeight: '400' }}>Simulación Colapso</h1>
                         <div className="flex mb-4">
							<div>
							Fecha Inicio
							<DatePicker className="ml-4" placeholder="Seleccionar fecha" />
							</div>
						</div>
                    <h1 style={{ fontSize: "24px", fontWeight: '400' }}>Leyenda</h1>
					<CardLeyenda />
				</div>
		
				{/* Contenedor del mapa */}
				<div className="flex-grow h-full overflow-hidden" style={{ height: "490px", width: "100%" }}>
					<MapaSimulacion />
				</div>
			</div>
		
			{/* Contenedor inferior con el botón y las pestañas */}
			<div
				className={`bg-gray-200 p-4 absolute bottom-0 w-full transition-transform duration-300 ${
				isSectionVisible ? "translate-y-80" : "translate-y-full"
				}`}
				style={{ zIndex: 10 }}
			>
				<Button
					className="absolute top-[-20px] left-1/2 transform -translate-x-1/2 z-20 bg-gray-300 border-gray-300 text-black"
					onClick={() => setIsSectionVisible(!isSectionVisible)}
					style={{ backgroundColor: '#e5e7eb', borderColor: '#e5e7eb', }}
				>
					{isSectionVisible ? "Ocultar" : "Mostrar"}
				</Button>


				{isSectionVisible && (
				<div className="mt-4">
                    <h1 style={{ fontSize: "16px", fontWeight: '400' }}>Camiones</h1>
                    <TablaFlota />
				</div>
				)}
			</div>
		</div>
	);
};

export default SimuColapso;
