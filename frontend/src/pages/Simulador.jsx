import { UploadOutlined } from "@ant-design/icons";
import { Button, Input, Modal } from "antd";
import { Upload } from "antd";

import axios from "axios";
import Papa from "papaparse";
import { useEffect, useRef, useState } from "react";
import rutaData from "/src/assets/data/Data.json";
import MapaSimulacion from "/src/components/Simulador/MapaSimulacion";
import ControlesSimulacion from "../components/Simulador/ControlesSimulacion.jsx";
import TablaSimulacion from "../components/Simulador/TablaSimulacion.jsx"; // Asegúrate de importar Papa Parse

const Simulador = () => {
	const almacenesPrincipales = ["150101", "130101", "040101"];
	const [currentTime, setCurrentTime] = useState(
		new Date().toLocaleTimeString()
	);
	const [numCamiones, setNumCamiones] = useState(0);
	const [numRutas, setNumRutas] = useState(0);

	const [simulacionActiva, setSimulacionActiva] = useState(false);
	const [simulacionIniciada, setSimulacionIniciada] = useState(false);
	const [simulacionTerminada, setSimulacionTerminada] = useState(false);
	const [resetRequerido, setResetRequerido] = useState(false); // Nuevo estado

	const [velocidad, setVelocidad] = useState(1); // Multiplicador de velocidad
	const intervalRef = useRef(null);

	// Estado para almacenar rutas y puntos de oficinas
	const [rutas, setRutas] = useState(rutaData);
	const [puntos, setPuntos] = useState([]);

	// Referencias para el estado de cada camión en movimiento
	const tramoIndexRef = useRef([]);
	const progresoTramoRef = useRef([]);
	const [currentPositions, setCurrentPositions] = useState({});

	useEffect(() => {
		// Actualizar la hora cada segundo
		const timer = setInterval(() => {
			setCurrentTime(new Date().toLocaleTimeString());
		}, 1000);
		return () => clearInterval(timer);
	}, []);

	useEffect(() => {
		handleUpdateStats(
			rutaData.length,
			rutaData.reduce((acc, ruta) => acc + ruta.tramos.length, 0)
		);
	}, []);

	// Cargar el CSV de oficinas
	const cargarCSV = (file) => {
		Papa.parse(file, {
			header: true,
			download: true,
			complete: (result) => {
				const data = result.data.map((oficina) => ({
					...oficina,
					almacenPrincipal: almacenesPrincipales.includes(oficina.id)
				}));
				setPuntos(data);
			}
		});
	};

	useEffect(() => {
		cargarCSV("/src/assets/data/oficinas.csv");
	});

	useEffect(() => {
		if (rutas.length > 0) {
			for (const ruta of rutaData) {
				const fix = { ...ruta.tramos[0] };
				fix.destino = { ...fix.origen };
				fix.distancia = 0;
				fix.tiempoLlegada = fix.tiempoSalida;
				fix.tiempoEspera = 0;
				ruta.tramos.unshift(fix);
			}
			tramoIndexRef.current = rutaData.map(() => 0);
			progresoTramoRef.current = rutaData.map(() => 0.01);
		}
		return () => clearInterval(intervalRef.current);
	}, [rutas]);

	// Función para actualizar los estados de camiones y rutas
	const handleUpdateStats = (camiones, rutas) => {
		setNumCamiones(camiones);
		setNumRutas(rutas);
	};

	// Función para cerrar el modal y ver la simulación terminada
	const verSimulacionTerminada = () => {
		setSimulacionIniciada(false);
		setSimulacionTerminada(false);
		setResetRequerido(true);
	};

	const iniciarSimulacion = () => {
		if (resetRequerido) {
			pararSimulacion();
			setResetRequerido(false);
		}
		setSimulacionTerminada(false);
		setSimulacionActiva(true);
		setSimulacionIniciada(true);
		iniciarSimulacionInterval();
	};

	const iniciarSimulacionInterval = () => {
		if (intervalRef.current) clearInterval(intervalRef.current);
		intervalRef.current = setInterval(moverCamiones, 1000 / velocidad);
	};

	const pausarSimulacion = () => {
		setSimulacionActiva(false);
		clearInterval(intervalRef.current);
	};

	const reanudarSimulacion = () => {
		setSimulacionActiva(true);
		iniciarSimulacionInterval();
	};

	const pararSimulacion = () => {
		setSimulacionIniciada(false);
		setSimulacionActiva(false);
		setSimulacionTerminada(false);
		setResetRequerido(false);
		clearInterval(intervalRef.current);
		resetearSimulacion();
	};

	const detenerSimulacion = () => {
		setSimulacionActiva(false);
		clearInterval(intervalRef.current);
		setSimulacionTerminada(true);
		setResetRequerido(true);
	};

	const resetearSimulacion = () => {
		tramoIndexRef.current = rutas.map(() => 0);
		progresoTramoRef.current = rutas.map(() => 0);
		setCurrentPositions(
			rutas.reduce((acc, ruta) => {
				const { codigo } = ruta.camion;
				acc[codigo] = {
					latitud: ruta.tramos[0].origen.latitud,
					longitud: ruta.tramos[0].origen.longitud
				};
				return acc;
			}, {})
		);
	};

	const acelerarSimulacion = () => {
		setVelocidad((prev) => {
			const nuevaVelocidad = Math.min(prev * 2, 16);
			iniciarSimulacionInterval();
			return nuevaVelocidad;
		});
	};

	const reducirSimulacion = () => {
		setVelocidad((prev) => {
			const nuevaVelocidad = Math.max(prev / 2, 0.25);
			iniciarSimulacionInterval();
			return nuevaVelocidad;
		});
	};

	const moverCamiones = () => {
		let allFinished = true;

		rutas.forEach((ruta, rutaIndex) => {
			const { codigo } = ruta.camion;
			const tramoIndex = tramoIndexRef.current[rutaIndex];
			const tramoActual = ruta.tramos[tramoIndex];

			if (!tramoActual) return;

			const {
				distancia,
				velocidad: velocidadTramo,
				origen,
				destino
			} = tramoActual;
			const tiempoTramo = ((distancia / velocidadTramo) * 1000) / velocidad;
			const progreso = progresoTramoRef.current[rutaIndex];

			const nuevaPosicion = {
				latitud: origen.latitud + (destino.latitud - origen.latitud) * progreso,
				longitud:
					origen.longitud + (destino.longitud - origen.longitud) * progreso
			};

			setCurrentPositions((prev) => ({
				...prev,
				[codigo]: nuevaPosicion
			}));

			progresoTramoRef.current[rutaIndex] += (1 / tiempoTramo) * velocidad;

			if (progresoTramoRef.current[rutaIndex] >= 1) {
				tramoIndexRef.current[rutaIndex]++;
				progresoTramoRef.current[rutaIndex] = 0.01;

				if (tramoIndexRef.current[rutaIndex] >= ruta.tramos.length) {
					setCurrentPositions((prev) => {
						const updated = { ...prev };
						delete updated[codigo];
						return updated;
					});
					tramoIndexRef.current[rutaIndex] = -1; // Marcar el camión como terminado
				} else allFinished = false;
			} else allFinished = false;
		});
		if (allFinished) detenerSimulacion();
	};

	// PARA SUBIR UN ARCHIVO
	const [selectedFile, setSelectedFile] = useState(null);

	const handleFileChange = (event) => {
		const file = event.target.files[0];
		if (file) {
			setSelectedFile(file);
		}
	};
	// Función para manejar el envío del archivo
	const handleSubmit = async () => {
		if (!selectedFile) {
			message.error("Por favor selecciona un archivo antes de enviar.");
			return;
		}
		const formData = new FormData();
		formData.append("archivo", selectedFile);

		try {
			const response = await axios.post(
				"http://localhost:8080/api/simulated-annealing/soluciones",
				formData,
				{
					headers: {
						"Content-Type": "multipart/form-data"
					}
				}
			);

			message.success("Archivo cargado exitosamente");
			console.log("Respuesta del servidor:", response.data);
			// Guardar las soluciones en el estado
			setRutas(response.data);
		} catch (error) {
			message.error("Error al cargar el archivo.");
			console.error("Error en la solicitud:", error);
		}
	};

	return (
		<div className="h-fit flex p-2">
			<div className="w-2/6">
				<Upload
					type="file"
					style={{
						width: "80%",
						marginBottom: "20px",
						padding: "20px"
					}}
					onChange={handleFileChange}
					accept=".txt"
				>
					<Button icon={<UploadOutlined />}>Subir archivo</Button>
				</Upload>

				<div className="mt-4">
					<TablaSimulacion data={rutas} />
				</div>
			</div>

			<div className="relative w-4/6 h-[92vh] m-auto border border-gray-300 shadow-lg rounded-lg">
				<MapaSimulacion
					simulacionActiva={simulacionActiva}
					simulacionIniciada={simulacionIniciada}
					resetRequerido={resetRequerido}
					velocidad={velocidad}
					rutas={rutas}
					puntos={puntos} // Pasamos los puntos al mapa
					currentPositions={currentPositions}
					tramoIndexRef={tramoIndexRef}
					progresoTramoRef={progresoTramoRef}
					onUpdateStats={handleUpdateStats}
				/>
				<div className="absolute bottom-4 right-4 z-10 bg-white p-4 rounded-lg shadow-lg">
					<ControlesSimulacion
						simulacionActiva={simulacionActiva}
						simulacionIniciada={simulacionIniciada}
						pausarSimulacion={pausarSimulacion}
						iniciarSimulacion={iniciarSimulacion}
						reanudarSimulacion={reanudarSimulacion}
						pararSimulacion={pararSimulacion}
						acelerarSimulacion={acelerarSimulacion}
						reducirSimulacion={reducirSimulacion}
						velocidad={velocidad}
					/>
				</div>
			</div>

			{/* Modal de Simulación Terminada */}
			<Modal
				title="La simulacion ha terminado"
				open={simulacionTerminada}
				closable={false}
				maskClosable={false}
				footer={[
					<Button
						key="ver"
						onClick={() => {
							verSimulacionTerminada();
						}}
					>
						Ver rutas
					</Button>,
					<Button
						key="reiniciar"
						type="primary"
						onClick={() => {
							pararSimulacion();
							setSimulacionTerminada(false);
						}}
					>
						Reiniciar simulación
					</Button>
				]}
			>
				<p>Puedes ver las rutas recorridas o reiniciar la simulación.</p>
			</Modal>
		</div>
	);
};

export default Simulador;
