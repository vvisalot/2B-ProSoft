import { Button, Tabs } from "antd";
import Tabla from "../components/Tabla";

const Planificador = () => {
	return (
		<div className="h-96 bg-stone-600">
			<div className="h-full bg-stone-600" />
			<div className="flex flex-col h-full">
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
							),
						},
						{
							label: "Rutas",
							key: "2",
							children: <Tabla />,
						},
						{
							label: "Flota",
							key: "3",
							children: <Tabla />,
						},
					]}
				/>
			</div>
		</div>
	);
};

export default Planificador;
