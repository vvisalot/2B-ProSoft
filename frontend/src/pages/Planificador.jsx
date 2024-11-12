import { Button, Tabs } from "antd";
import MapaPeru from "../components/MapaPeru";
import Tabla from "../components/Tabla";
import PedidosPage from "../pages/Pedidos";
import RutasPage from "../pages/Rutas";
import FlotaPage from "../pages/Flota";

const Planificador = () => {
	return (
		<div className="flex flex-col lg:flex-row h-full">
			{/* Contenedor de la mitad izquierda */}
			<div className="w-full lg:w-1/2">
				<Tabs
					defaultActiveKey="1"
					className="w-full"
					onChange={(key) => {
						// Cambia de ruta según la pestaña seleccionada
						if (key === "1") navigate("/pedidos");
						else if (key === "2") navigate("/rutas");
						else if (key === "3") navigate("/flota");
					}}
					items={[
						{
							label: "Pedidos",
							key: "1",
							children: <PedidosPage />,
						},
						{
							label: "Rutas",
							key: "2",
							children: <RutasPage />,
						},
						{
							label: "Flota",
							key: "3",
							children: <FlotaPage />,
						},
					]}
				/>
			</div>

			{/* Contenedor de la mitad derecha */}
			<div className="w-full lg:w-1/2 h-full">
				<MapaPeru />
			</div>
		</div>
	);
};

export default Planificador;
