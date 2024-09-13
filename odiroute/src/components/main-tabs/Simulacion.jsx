import { Tabs } from "antd";
import TabPane from "antd/es/tabs/TabPane";

const Simulacion = () => {
	return (
		<div className="h-96 bg-stone-600">
			<div className="h-full bg-stone-600" />
			<div className="flex flex-col h-full">
				<Tabs defaultActiveKey="1" className="w-full ">
					<TabPane tab="Por fecha" key="1" />
					<TabPane tab="Colapso" key="2" />
				</Tabs>
			</div>
		</div>
	);
};

export default Simulacion;
