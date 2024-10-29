import { Table } from "antd";

const data = [
	{ id: 1, name: "John", age: 25 },
	{ id: 2, name: "Jane", age: 30 },
	{ id: 3, name: "Bob", age: 35 },
];

const columns = [
	{ title: "ID del Pedido", dataIndex: "id", key: "id" },
	{ title: "ID del Cliente", dataIndex: "client", key: "client" },
	{ title: "Tiempo Estimado", dataIndex: "time1", key: "time1" },
	{ title: "Tiempo Real", dataIndex: "time2", key: "time2" },
	{ title: "Estado de orden", dataIndex: "estado", key: "estado" },
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
