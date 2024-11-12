import { Button, Tabs } from "antd";
import MapaPeru from "../components/MapaPeru";
import TablaSimulacion from "../components/Simulador/TablaSimulacion.jsx";

const Planificador = () => {
	return (
		<div className="flex h-full">
			<div className="w-full">
                
                <div className="flex">
				<Button className="mr-2 bg-black" type="primary">
					Nuevo Pedido
				</Button>
				<Button> Importar Pedidos </Button>
				</div>
				<TablaSimulacion />

			</div>
		</div>
	);
};

export default Planificador;
