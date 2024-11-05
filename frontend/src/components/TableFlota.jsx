import { Table } from "antd";

const data = [
	{ id: 1, name: "John", age: 25 },
	{ id: 2, name: "Jane", age: 30 },
	{ id: 3, name: "Bob", age: 35 },
];

const columns = [
	{ title: "ID de Camion", dataIndex: "id", key: "id" },
	{ title: "Tipo de Camion", dataIndex: "client", key: "client" },
	{ title: "Próximo Mantenimiento", dataIndex: "mantenimiento", key: "mantenimiento" },
    { title: "Estado", dataIndex: "estado", key: "estado" },
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
