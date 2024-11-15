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
import Simulador from "./pages/Simulador.jsx";

const App = () => {
	const [isLandingPage, setIsLandingPage] = useState(true);
	const [selectedTab, setSelectedTab] = useState("1");
	const [simulationPage, setSimulationPage] = useState(null);

	const handleMenuClick = (e) => {
		if (e.key === "1") {
			setSimulationPage(<SimuSemanal />);
			message.success("Redirigiendo a Simulación Semanal");
		} else if (e.key === "2") {
			setSimulationPage(<SimuColapso />);
			message.success("Redirigiendo a Simulación de Colapso");
		}
		setSelectedTab("2");
	};

	const simulationMenuItems = [
		{ label: "Simulación Semanal", key: "1" },
		{ label: "Simulación de Colapso", key: "2" }
	];

	return (
		<Layout style={{ height: "100vh" }}>
			{/*{isLandingPage ? (*/}
			{/*	<LandingPage onEnter={() => setIsLandingPage(false)} />*/}
			{/*) : (*/}
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
							activeKey={selectedTab}
							onChange={(key) => setSelectedTab(key)}
							className="ml-6 mr-6 w-full flex-grow"
							items={[
								{
									label: "Planificador",
									key: "1",
									children: <Planificador />,
								},
								{
									label:"Simulacion",
									key: "2",
									children: <Simulador/>,
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
			{/*)}*/}
		</Layout>
	);
};

export default App;
