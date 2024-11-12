import { Table } from "antd";
import { useEffect, useState } from "react";
import rutaData from "/src/assets/data/Data.json"; // JSON para los camiones y rutas

const columns = [
	{ title: "ID de Camion", dataIndex: "codigo", key: "codigo", width: 20 },
	{ title: "Tipo de Camion", dataIndex: "tipo", key: "tipo", width: 30 },
	{ title: "Capacidad", dataIndex: "capacidad", key: "capacidad", width: 30 },
	{ title: "Carga Actual", dataIndex: "cargaActual", key: "cargaActual", width: 80 },
	{ title: "Próximo Mantenimiento", dataIndex: "mantenimiento", key: "mantenimiento", width: 100 },
	{ title: "Estado", dataIndex: "estado", key: "estado", width: 40 },
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

	// Estilos en línea para personalizar la tabla
	const customStyles = {
		fontSize: "12px",  // Tamaño de letra más pequeño
		padding: "8px",    // Menos espacio dentro de las celdas
	};

	return (
		<Table
			dataSource={data}
			columns={columns}
			scroll={{ x: 150 }} // Ajuste del scroll horizontal
			pagination={{ pageSize: 5 }}
			className="pt-4"
			style={customStyles}
		/>
	);
};

export default Tabla;
