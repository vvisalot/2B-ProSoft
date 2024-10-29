import { useState, useEffect } from "react";
import { DatePicker, Tabs, Button } from "antd";
import Tabla from "../components/Tabla";
import MapaPeru from "../components/MapaPeru";

const Simulacion = () => {

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
		<div className="h-full flex flex-col">
			
			<div className="w-full bg-blue-300 p-4 flex justify-center items-center">
				<div className="flex justify-between items-center">
					<h2>Hora</h2>
					<div className="text-lg">{currentTime}</div>
				</div>
			</div>
			
			<div className="flex flex-1">
				{/* Contenedor izquierdo */}
				<div className="w-1/5 bg-gray-200 p-4">
					<div>Leyenda</div>
				</div>

				{/* Contenedor del mapa */}
				<div className="w-full h-full">
					<MapaPeru />
				</div>
			</div>

			{/* Contenedor inferior con el botón y las pestañas */}
			<div className="bg-stone-600 p-4 relative">
				<Button
					className="absolute top-2 right-2 z-10"
					onClick={() => setIsSectionVisible(!isSectionVisible)}
				>
					{isSectionVisible ? "Ocultar" : "Mostrar"}
				</Button>

				{isSectionVisible && (
					<div className="mt-4">
						<Tabs
							defaultActiveKey="1"
							className="w-full"
							items={[
								{
									label: "Por fecha",
									key: "1",
									children: (
										<>
											<div className="flex mb-4">
												<div>
													Fecha Inicio
													<DatePicker className="ml-4" placeholder="Seleccionar fecha" />
												</div>
												<div className="pl-4">
													Fecha Fin
													<DatePicker className="ml-4" placeholder="Seleccionar fecha" />
												</div>
											</div>
											<Tabla />
										</>
									),
								},
								{
									label: "Colapso",
									key: "2",
									children: (
										<>
											<div className="flex mb-4">
												<div>
													Fecha Inicio
													<DatePicker className="ml-4" />
												</div>
												<div className="pl-4">
													Fecha Fin
													<DatePicker className="ml-4" />
												</div>
											</div>
											<Tabla />
										</>
									),
								},
							]}
						/>
					</div>
				)}
			</div>
		</div>
	);
};

export default Simulacion;
