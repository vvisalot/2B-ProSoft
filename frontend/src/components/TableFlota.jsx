import { Table } from "antd";


const columns = [
	{ title: "Próximo Mantenimiento", dataIndex: "mantenimiento", key: "mantenimiento" },
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
