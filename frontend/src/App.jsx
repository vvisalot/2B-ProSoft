import { Layout, Tabs } from "antd";
import { Content, Header } from "antd/es/layout/layout";
import Analiticas from "./pages/Analiticas";
import Configuracion from "./pages/Configuracion";
import Planificador from "./pages/Planificador";
import Simulacion from "./pages/Simulacion";
import logo from "./assets/odipark.svg";

const App = () => {
	return (
		<Layout style={{ height: "100vh" }}>
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
								label: "Simulación",
								key: "2",
								children: <Simulacion />,
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
		</Layout>
	);
};

export default App;
