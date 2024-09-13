import { Tabs } from "antd";
import TabPane from "antd/es/tabs/TabPane";

const Planificador = () => {
	return (
		<div className="h-96 bg-stone-600">
			<div className="h-full bg-stone-600" />
			<div className="flex flex-col h-full">
				<Tabs defaultActiveKey="1" className="w-full ">
					<TabPane tab="Pedidos" key="1" />
					<TabPane tab="Rutas" key="2" />
					<TabPane tab="Flota" key="3" />
				</Tabs>
			</div>
		</div>
	);
};

export default Planificador;
