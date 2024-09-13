import { Layout, Tabs } from "antd";
import { Content, Header } from "antd/es/layout/layout";
import Analiticas from "./components/main-tabs/Analiticas";
import Configuracion from "./components/main-tabs/Configuracion";
import Planificador from "./components/main-tabs/Planificador";
import Simulacion from "./components/main-tabs/Simulacion";

const { TabPane } = Tabs;

const App = () => {
	return (
		<Layout style={{ height: "100vh" }}>
			<div className="min-h-screen flex flex-col">
				<Header className="bg-gray-700 text-white pl-6 pr-6 pt-4 pb-4">
					<h1 className="text-3xl">OdiparPack</h1>
				</Header>

				<Content className="flex-grow flex bg-white">
					<Tabs defaultActiveKey="1" className="ml-6 mr-6 w-full flex-grow">
						<TabPane tab="Planificador" key="1">
							<Planificador />
						</TabPane>

						<TabPane tab="Simulación" key="2">
							<Simulacion />
						</TabPane>

						<TabPane tab="Analíticas" key="3">
							<Analiticas />
						</TabPane>

						<TabPane tab="Configuración" key="4">
							<Configuracion />
						</TabPane>
					</Tabs>
				</Content>
			</div>
		</Layout>
	);
};

export default App;
