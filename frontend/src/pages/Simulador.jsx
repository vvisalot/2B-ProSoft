import { DownOutlined } from "@ant-design/icons";
import {
	Button,
	DatePicker,
	Dropdown,
	Input,
	Modal,
	Space,
	TimePicker,
	Typography,
} from "antd";
import dayjs from "dayjs";
import customParseFormat from "dayjs/plugin/customParseFormat";
import { React, useEffect, useRef, useState } from "react";
import rutaData from "/src/assets/data/Data.json";
import MapaSimulacion from "/src/components/Simulador/MapaSimulacion";
import ControlesSimulacion from "../components/Simulador/ControlesSimulacion.jsx";
import InformacionSimulacion from "../components/Simulador/InformacionSimulacion.jsx"; // Asegúrate de importar Papa Parse
import TablaSimulacion from "../components/Simulador/TablaSimulacion.jsx";
dayjs.extend(customParseFormat);

const Simulador = () => {
	const [numCamiones, setNumCamiones] = useState(0);
	const [numRutas, setNumRutas] = useState(0);

	const [rutas, setRutas] = useState([]);

	const tramoIndexRef = useRef([]);
	const progresoTramoRef = useRef([]);
	const [currentPositions, setCurrentPositions] = useState({});

	const [simulacionActiva, setSimulacionActiva] = useState(false);
	const [simulacionIniciada, setSimulacionIniciada] = useState(false);
	const [resetRequerido, setResetRequerido] = useState(false); // Nuevo estado
	const [velocidad, setVelocidad] = useState(1); // Multiplicador de velocidad
	const [currentTime, setCurrentTime] = useState("2024-03-14T00:00:00");
	const [simulatedTime, setSimulatedTime] = useState(dayjs(currentTime));
	const [selectedDate, setSelectedDate] = useState(null);
	const [selectedTime, setSelectedTime] = useState(null);
	const [selectedItem, setSelectedItem] = useState("Simulación");
	const items = [
		{
			key: "1",
			label: "Semanal",
			onClick: () => setSelectedItem("Semanal"),
		},
		{
			key: "2",
			label: "Colapso",
			onClick: () => setSelectedItem("Colapso"),
			disabled: true, //por ahora para solo tener semanal
		},
	];

	//Validación para solo tomar rango de marzo 2024 a mayo 2025
	const startDate = dayjs("2024-03-01");
	const endDate = dayjs("2025-05-01");
	const disabledDate = (current) => {
		return current && (current < startDate || current > endDate);
	};
	const currentTimeRef = useRef(currentTime);
	/*
    //Constantes para variables de fecha y tiempo
    const onChangeDate = (date, dateString) => {
        if (date) {
            const isoDate = date.toISOString().split('T')[0]; // Solo la fecha en formato YYYY-MM-DD
            setCurrentTime(`${isoDate}T00:00:00`); // Combinar con tiempo inicial predeterminado
            console.log("Fecha seleccionada (ISO):", `${isoDate}T00:00:00`);
        } else {
            console.error("No se seleccionó ninguna fecha.");
        }
    };
    const onChangeTime = (time, timeString) => {
        if (time) {
            const isoDate = currentTime.split('T')[0]; // Extraer la fecha actual
            setCurrentTime(`${isoDate}T${timeString}`); // Combinar con la hora seleccionada
            console.log("Fecha y hora seleccionada (ISO):", `${isoDate}T${timeString}`);
        } else {
            console.error("No se seleccionó ningún tiempo.");
        }
    };

    */

	const onChangeDate = (date) => {
		if (date) {
			setSelectedDate(date.format("YYYY-MM-DD"));
		}
	};

	const onChangeTime = (time) => {
		if (time) {
			setSelectedTime(time.format("HH:mm:ss"));
		}
	};

	useEffect(() => {
		if (selectedDate && selectedTime) {
			setCurrentTime(`${selectedDate}T${selectedTime}`);
			console.log(
				"Fecha y hora combinadas:",
				`${selectedDate}T${selectedTime}`,
			);
		}
	}, [selectedDate, selectedTime]);

	const moverCamiones = (velocidad, onSimulacionTerminada) => {
		let allFinished = true;

		rutas.forEach((ruta, rutaIndex) => {
			const { codigo } = ruta.camion;
			const tramoIndex = tramoIndexRef.current[rutaIndex];
			const tramoActual = ruta.tramos[tramoIndex];

			if (!tramoActual) return; // Si no hay tramos restantes, salta esta ruta
			const {
				distancia,
				velocidad: velocidadTramo,
				origen,
				destino,
				tiempoSalida,
			} = tramoActual;
			console.log("Comparando tiempos:", {
				currentTime: currentTimeRef.current,
				tiempoSalida,
				isBefore: dayjs(currentTimeRef.current).isBefore(dayjs(tiempoSalida)),
			});
			if (dayjs(currentTimeRef.current).isBefore(dayjs(tiempoSalida))) {
				allFinished = false; // Aún no puede moverse, pero la simulación sigue activa
				return; // Ignorar este camión hasta que llegue su tiempo de salida
			}
			console.log(
				"Moviendo camión",
				codigo,
				"en el tramo",
				tramoIndex,
				"de la ruta",
				rutaIndex,
			);
			// Tiempo en milisegundos para completar el tramo
			const tiempoTramo = ((distancia / velocidadTramo) * 1000) / velocidad;
			const progreso = progresoTramoRef.current[rutaIndex];

			// Calcula la nueva posición del camión
			const nuevaPosicion = {
				latitud: origen.latitud + (destino.latitud - origen.latitud) * progreso,
				longitud:
					origen.longitud + (destino.longitud - origen.longitud) * progreso,
			};

			// Actualiza la posición actual del camión
			setCurrentPositions((prev) => ({
				...prev,
				[codigo]: nuevaPosicion,
			}));

			// Avanza el progreso en el tramo actual
			progresoTramoRef.current[rutaIndex] +=
				(1 / tiempoTramo) * (6 / 60) * velocidad;

			if (progresoTramoRef.current[rutaIndex] >= 1) {
				// Si se completa el tramo, pasa al siguiente
				tramoIndexRef.current[rutaIndex]++;
				progresoTramoRef.current[rutaIndex] = 0.01;

				// Si ya no hay más tramos, marca el camión como terminado
				if (tramoIndexRef.current[rutaIndex] >= ruta.tramos.length) {
					setCurrentPositions((prev) => {
						const updated = { ...prev };
						delete updated[codigo];
						return updated;
					});
					tramoIndexRef.current[rutaIndex] = -1; // Indica que terminó la ruta
				} else {
					allFinished = false;
				}
			} else {
				allFinished = false;
			}
		});

		if (allFinished) {
			//console.log("Simulación terminada");
			onSimulacionTerminada();
		}
	};

	useEffect(() => {
		currentTimeRef.current = currentTime;
	}, [currentTime]);

	useEffect(() => {
		if (simulacionActiva) {
			const interval = setInterval(() => {
				moverCamiones(velocidad, () => {
					//console.log("Simulación detenida desde moverCamiones.");
					setSimulacionActiva(false);
				});
			}, 1000);

			return () => clearInterval(interval);
		}
	}, [simulacionActiva, velocidad, rutas, currentTimeRef.current]);

	const resetearSimulacion = () => {
		tramoIndexRef.current = rutas.map(() => 0);
		progresoTramoRef.current = rutas.map(() => 0.01);
		setCurrentPositions(
			rutas.reduce((acc, ruta) => {
				const { codigo } = ruta.camion;
				const primerTramo = ruta.tramos[0];
				if (new Date(currentTime) >= new Date(primerTramo.tiempoSalida)) {
					acc[codigo] = {
						latitud: primerTramo.origen.latitud,
						longitud: primerTramo.origen.longitud,
					};
				}
				return acc;
			}, {}),
		);
	};

	// useEffect(() => {
	//     const timer = setInterval(() => {
	//         setCurrentTime(new Date().toLocaleTimeString());
	//     }, 1000);
	//     return () => clearInterval(timer);
	// }, []);

	useEffect(() => {
		handleUpdateStats(
			rutaData.length,
			rutaData.reduce((acc, ruta) => acc + ruta.tramos.length, 0),
		);
	}, []);

	// useEffect(() => {
	//     //imprimir en consola los tramos de cada ruta
	//     console.log("Tramos de cada ruta:");
	//     rutas.forEach((ruta, index) => {
	//         console.log(`Ruta ${index} tiene ${ruta.tramos.length} tramos`);
	//         console.log(ruta.tramos);
	//     });
	// }, [rutas]);

	//Se cargan los datos de la solucion del algoritmo.
	useEffect(() => {
		if (rutas.length > 0) {
			// rutas.forEach((ruta, index) => {
			//     console.log(`Ruta ${index} tiene ${ruta.tramos.length} tramos:`);
			//     console.log(ruta.tramos); // el último tramo se pinta?
			// });

			tramoIndexRef.current = rutas.map(() => 0); // Índices iniciales de los tramos
			progresoTramoRef.current = rutas.map(() => 0.01); // Progreso inicial por tramo
			setCurrentPositions(
				rutas.reduce((acc, ruta) => {
					const { codigo } = ruta.camion;
					acc[codigo] = {
						latitud: ruta.tramos[0].origen.latitud,
						longitud: ruta.tramos[0].origen.longitud,
					};
					return acc;
				}, {}),
			);
		}
	}, [rutas]); // Se asegura que se actualice cada vez que cambien las rutas

	// Simulación de tiempo acelerado
	useEffect(() => {
		let interval;

		if (simulacionActiva) {
			interval = setInterval(() => {
				setCurrentTime((prevTime) => {
					const currentTimeObj = dayjs(prevTime); // Convertir a objeto dayjs
					const newTimeObj = currentTimeObj.add(1, "minute"); // Avanzar 1 minuto real como prueba inicial
					const newTimeString = newTimeObj.toISOString(); // Convertir a cadena ISO
					if (newTimeObj.minute() === 0 && newTimeObj.second() === 0) {
						console.log("Tiempo simulado desde simulador jsx:", newTimeString);
					}
					return newTimeObj.toISOString(); // Actualizar el estado con el nuevo tiempo
				});
			}, 250); // Cada 250ms para simular tiempo acelerado (4x real)
		} else {
			clearInterval(interval);
		}

		return () => clearInterval(interval);
	}, [simulacionActiva]);

	// Función para actualizar los estados de camiones y rutas
	const handleUpdateStats = (camiones, rutas) => {
		setNumCamiones(camiones);
		setNumRutas(rutas);
	};

	return (
		<div className="h-fit flex p-2">
			<div className="w-5/12">
				<InformacionSimulacion
					currentTime={currentTime}
					simulacionActiva={simulacionActiva}
					velocidad={velocidad}
					setSimulatedTime={setSimulatedTime}
				/>
				<TablaSimulacion data={rutas} />
			</div>

			<div className="relative w-7/12 h-100 border border-gray-300 shadow-lg rounded-lg">
				<div
					className="w-full flex justify-left items-center"
					style={{ height: "5vh", marginTop: "1vh", marginBottom: "1vh" }}
				>
					<h1
						style={{
							fontSize: "1rem",
							fontWeight: "400",
							marginLeft: "1vh",
							marginRight: "1.5vh",
						}}
					>
						Elija el tipo de simulación:
					</h1>
					<Dropdown
						menu={{
							items,
							selectable: true,
							defaultSelectedKeys: ["2"],
						}}
					>
						<Typography.Link>
							<Space>
								{selectedItem}
								<DownOutlined />
							</Space>
						</Typography.Link>
					</Dropdown>

					<Space direction="vertical" style={{ marginLeft: "5vh" }}>
						<DatePicker
							onChange={onChangeDate}
							disabledDate={disabledDate}
							defaultValue={dayjs("2024-03-14", "YYYY-MM-DD")}
						/>
					</Space>
					<TimePicker
						style={{ marginLeft: "2vh" }}
						onChange={onChangeTime}
						defaultOpenValue={dayjs("00:00:00", "HH:mm:ss")}
					/>
				</div>

				<MapaSimulacion
					rutas={rutas}
					setRutas={setRutas}
					currentPositions={currentPositions}
					tramoIndexRef={tramoIndexRef}
					progresoTramoRef={progresoTramoRef}
					onUpdateStats={handleUpdateStats}
					simulacionActiva={simulacionActiva}
					simulacionIniciada={simulacionIniciada}
					resetRequerido={resetRequerido}
					velocidad={velocidad}
					simulatedTime={simulatedTime}
				/>
				<div className="absolute bottom-4 right-4 z-10 bg-white p-4 rounded-lg shadow-lg">
					<ControlesSimulacion
						rutas={rutas}
						setRutas={setRutas}
						moverCamiones={moverCamiones}
						resetearSimulacion={resetearSimulacion}
						currentTime={currentTime}
						setCurrentTime={setCurrentTime}
						onSimulacionStateChange={(state) => {
							setSimulacionActiva(state.simulacionActiva);
							setSimulacionIniciada(state.simulacionIniciada);
							setResetRequerido(state.resetRequerido);
							setVelocidad(state.velocidad);
						}}
					/>
				</div>
			</div>
		</div>
	);
};

export default Simulador;
