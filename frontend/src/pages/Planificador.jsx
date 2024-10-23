import { Button, Tabs } from "antd";
import MapaPeru from "../components/MapaPeru";
import Tabla from "../components/Tabla";

const Planificador = () => {
	return (
		<div className="flex h-full">
			<div className="w-1/2">
				<Tabs
					defaultActiveKey="1"
					className="w-full"
					items={[
						{
							label: "Pedidos",
							key: "1",
							children: (
								<>
									<div className="flex">
										<Button className="mr-2 bg-black" type="primary">
											Nuevo Pedido
										</Button>
										<Button> Importar Pedidos </Button>
									</div>
									<Tabla />
								</>
							)
						},
						{
							label: "Rutas",
							key: "2",
							children: <Tabla />
						},
						{
							label: "Flota",
							key: "3",
							children: <Tabla />
						}
					]}
				/>
			</div>
			<div className="w-1/2 h-full">
				<MapaPeru />
			</div>
		</div>
	);
};

export default Planificador;
