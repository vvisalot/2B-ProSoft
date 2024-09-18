import { Table } from "antd";

const data = [
	{ id: 1, name: "John", age: 25 },
	{ id: 2, name: "Jane", age: 30 },
	{ id: 3, name: "Bob", age: 35 },
];

const columns = [
	{ title: "ID", dataIndex: "id", key: "id" },
	{ title: "Name", dataIndex: "name", key: "name" },
	{ title: "Age", dataIndex: "age", key: "age" },
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
