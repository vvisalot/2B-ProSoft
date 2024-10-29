import { Table } from "antd";

const data = [
	{ id: 1, name: "John", age: 25 },
	{ id: 2, name: "Jane", age: 30 },
	{ id: 3, name: "Bob", age: 35 },
];

const columns = [
	{ title: "ID de Ruta", dataIndex: "id", key: "id" },
	{ title: "Ubigeo Origen", dataIndex: "origen", key: "origen" },
	{ title: "Ubigeo Destino", dataIndex: "destino", key: "destino" },
	{ title: "Detalle", dataIndex: "detalle", key: "detalle" },
	// Add more columns as needed
];

const Tabla = () => {
	return (
		<Table
			className="pt-4"
			dataSource={data.map((item) => ({ ...item, key: item.id }))}
			columns={columns}
		/>
	);
};

export default Tabla;
