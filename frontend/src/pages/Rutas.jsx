import { Button, Tabs } from "antd";
import MapaPeru from "../components/MapaPeru";
import TablaRutas from "../components/TablaRutas";

const Planificador = () => {
	return (
		<div className="flex h-full">
			<div className="w-full">
                
                <div className="flex">
				<Button className="mr-2 bg-black" type="primary">
					Nueva Ruta
				</Button>
				</div>
				<TablaRutas />

			</div>
		</div>
	);
};

export default Planificador;
