import { useState, useEffect } from "react";
import { DatePicker, Tabs, Button, Dropdown, Menu } from "antd";
import TablaFlota from "../components/TableFlota";
import MapaPeru from "../components/MapaPeru";
import CardLeyenda from "../cards/CardLeyenda";
import Semanal from "../pages/Simulador/SimuSemanal"
import Colapso from "../pages/Simulador/SimuColapso"
import Title from "antd/es/skeleton/Title";

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


	const menu = (
		<Menu>
			<Menu.Item key="1">
				<span>Opción 1</span>
			</Menu.Item>
			<Menu.Item key="2">
				<span>Opción 2</span>
			</Menu.Item>
		</Menu>
	);

	return (
		<div className="h-full flex flex-col relative">
			{/* Contenedor Superior de hora */}
			<div className="w-full flex justify-center items-center"  style={{ height: "25px", backgroundColor: '#ffd700' }}>

				<div className="flex justify-between items-center">
					<h2>Hora</h2>
					<div className="text-lg">{currentTime}</div>
				</div>
			</div>
		
			<div className="flex flex-1">
				{/* Contenedor izquierdo */}
				<div className="p-4 overflow-hidden" style={{ height: "490px", width: "400px", backgroundColor: '#fafafa' }}>
					<h1 style={{ fontSize: "24px", fontWeight: '400' }}>Leyenda</h1>
					<CardLeyenda />
				</div>
		
				{/* Contenedor del mapa */}
				<div className="flex-grow h-full overflow-hidden" style={{ height: "490px", width: "100%" }}>
					<MapaPeru />
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

				{/* Botón de Simulación con Dropdown */}
				<Dropdown overlay={menu} trigger={['click']}>
					<Button type="primary" style={{ marginTop: "10px" }}>Simulación</Button>
				</Dropdown>

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
							<TablaFlota />
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
								<DatePicker className="ml-4" placeholder="Seleccionar fecha"/>
								</div>
							</div>
							<TablaFlota />
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
