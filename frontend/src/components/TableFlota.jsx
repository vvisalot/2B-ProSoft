import { Table } from "antd";
import { useEffect, useState } from "react";
import rutaData from "/src/assets/data/Data.json"; // JSON para los camiones y rutas

// const data = [
// 	{ id: 1, name: "John", age: 25 },
// 	{ id: 2, name: "Jane", age: 30 },
// 	{ id: 3, name: "Bob", age: 35 },
// ];

// const columns = [
// 	{ title: "ID de Camion", dataIndex: "id", key: "id" },
// 	{ title: "Tipo de Camion", dataIndex: "client", key: "client" },
// 	{ title: "Próximo Mantenimiento", dataIndex: "mantenimiento", key: "mantenimiento" },
//     { title: "Estado", dataIndex: "estado", key: "estado" },
// 	{ title: "Detalle", dataIndex: "time2", key: "time2" },
// 	// Add more columns as needed
// ];

const columns = [
	{ title: "ID de Camion", dataIndex: "codigo", key: "codigo" },
	{ title: "Tipo de Camion", dataIndex: "tipo", key: "tipo" },
	{ title: "Capacidad", dataIndex: "capacidad", key: "capacidad" },
	{ title: "Carga Actual", dataIndex: "cargaActual", key: "cargaActual" },
	{ title: "Próximo Mantenimiento", dataIndex: "mantenimiento", key: "mantenimiento" },
	{ title: "Estado", dataIndex: "estado", key: "estado" },
];

const Tabla = () => {
	const [data, setData] = useState([]);

	// Simular datos de tabla con JSON y formatear los datos de los camiones para la tabla
	useEffect(() => {
		const formattedData = rutaData.map((entry) => ({
			key: entry.camion.codigo, // codigo como identificador
			codigo: entry.camion.codigo,
			tipo: entry.camion.tipo,
			capacidad: entry.camion.capacidad,
			cargaActual: entry.camion.cargaActual,
			mantenimiento: entry.camion.mantenimiento || "No definido",
			estado: entry.camion.estado || "Desconocido",
		}));

		setData(formattedData);
	}, []);
	return (
		<Table
			className="pt-4"
			dataSource={data.map((item) => ({ ...item, key: item.id }))}
			columns={columns}
		/>
	);
};

export default Tabla;
