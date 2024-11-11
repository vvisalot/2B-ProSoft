import { Button, Tabs } from "antd";
import MapaPeru from "../components/MapaPeru";
import TablaSimulacion from "../components/TablaSimulacion.jsx";
import PedidosPage from "../pages/Pedidos";
import RutasPage from "../pages/Rutas";
import FlotaPage from "../pages/Flota";

const Planificador = () => {
	return (
		<div className="flex flex-col lg:flex-row h-full">
			{/* Contenedor de la mitad derecha */}
			<div className="w-full lg:w-1/2 h-full">
				{/*<MapaPeru />*/}
			</div>
		</div>
	);
};

export default Planificador;
