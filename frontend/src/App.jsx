import React, { useState } from "react";
import { Layout, Tabs, Dropdown, Menu, message } from "antd";
import { Content, Header } from "antd/es/layout/layout";
import Analiticas from "./pages/Analiticas";
import Configuracion from "./pages/Configuracion";
import Planificador from "./pages/Planificador";
import SimuSemanal from "./pages/Simulador/SimuSemanal";
import SimuColapso from "./pages/Simulador/SimuColapso";
import LandingPage from "./pages/LandingPage"; 
import logo from "./assets/odipark.svg";

const App = () => {
	const [isLandingPage, setIsLandingPage] = useState(true);
	const [selectedTab, setSelectedTab] = useState("1"); // Estado para controlar la pestaña seleccionada
	const [simulationPage, setSimulationPage] = useState(null); // Estado para controlar la página de simulación seleccionada

	// Función para manejar la selección de la opción en el menú desplegable
	const handleMenuClick = (e) => {
		if (e.key === "1") {
			setSimulationPage(<SimuSemanal />);
			message.success("Redirigiendo a Simulación Semanal");
		} else if (e.key === "2") {
			setSimulationPage(<SimuColapso />);
			message.success("Redirigiendo a Simulación de Colapso");
		}
		setSelectedTab("2"); // Cambia a la pestaña de Simulación
	};

	// Menú desplegable para la pestaña de Simulación
	const simulationMenu = (
		<Menu onClick={handleMenuClick}>
			<Menu.Item key="1">Simulación Semanal</Menu.Item>
			<Menu.Item key="2">Simulación de Colapso</Menu.Item>
		</Menu>
	);

	return (
		<Layout style={{ height: "100vh" }}>
			{isLandingPage ? (
				// Mostrar la landing page si el estado isLandingPage es true
				<LandingPage onEnter={() => setIsLandingPage(false)} />
			) : (
				// Mostrar la aplicación si el estado isLandingPage es false
				<div className="min-h-screen flex flex-col">
					<Header className="bg-gray-700 text-white pl-6 pr-6 pt-4 pb-4 flex items-center">
						<img
							src={logo}
							alt="OdiparPack Logo"
							style={{ height: "40px", marginRight: "16px" }}
						/>
						<h1 className="text-3xl">OdiparPack</h1>
					</Header>

					<Content className="flex-grow flex bg-white">
						<Tabs
							defaultActiveKey="1"
							className="ml-6 mr-6 w-full flex-grow"
							items={[
								{
									label: "Planificador",
									key: "1",
									children: <Planificador />,
								},
								{
									label: (
										<Dropdown overlay={simulationMenu} trigger={["hover"]}>
											<span style={{ cursor: "pointer" }}>Simulación</span>
										</Dropdown>
									),
									key: "2",
									children: simulationPage, 
								},
								{
									label: "Analíticas",
									key: "3",
									children: <Analiticas />,
								},
								{
									label: "Configuración",
									key: "4",
									children: <Configuracion />,
								},
							]}
						/>
					</Content>
				</div>
			)}
		</Layout>
	);
};

export default App;
