import { DownOutlined } from '@ant-design/icons';
import {Button, DatePicker, Dropdown, Input, Modal, Space, TimePicker, Typography} from "antd";
import dayjs from 'dayjs';
import customParseFormat from 'dayjs/plugin/customParseFormat';
import {React, useEffect, useRef, useState} from "react";
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
    const [simulatedTime, setSimulatedTime] = useState(dayjs(currentTime))
    
    const [selectedDate, setSelectedDate] = useState(null);
    const [selectedTime, setSelectedTime] = useState(null);
    const [selectedItem, setSelectedItem] = useState('Simulación');
    const items = [
        {
          key: '1',
          label: 'Semanal',
          onClick: () => setSelectedItem('Semanal'),
        },
        {
          key: '2',
          label: 'Colapso',
          onClick: () => setSelectedItem('Colapso'),
          disabled: true, //por ahora para solo tener semanal
        },
      ];
    



    //Validación para solo tomar rango de marzo 2024 a mayo 2025
    const startDate = dayjs('2024-03-01');
    const endDate = dayjs('2025-05-01');
    const disabledDate = (current) => {
        return current && (current < startDate || current > endDate);
    };

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
            console.log("Fecha y hora combinadas:", `${selectedDate}T${selectedTime}`);
        }
    }, [selectedDate, selectedTime]);



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

	//Se cargan los datos de la solucion del algoritmo.
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
			setRutas(rutas);
			tramoIndexRef.current = rutaData.map(() => 0);
			progresoTramoRef.current = rutaData.map(() => 0.01);
		}
	}, [rutas]);
    
    const moverCamiones = (velocidad, onSimulacionTerminada, simulatedTime) => {
        let allFinished = true;
    
        rutas.forEach((ruta, rutaIndex) => {
            const { codigo } = ruta.camion;
            const tramoIndex = tramoIndexRef.current[rutaIndex];
            const tramoActual = ruta.tramos[tramoIndex];
    
            if (!tramoActual) return; // No hay más tramos para este camión, así que salimos
    
            const { distancia, velocidad: velocidadTramo, origen, destino, tiempoSalida, tiempoLlegada } = tramoActual;
            
            console.log('Camión:', codigo);
            console.log('Tiempo de salida del tramo:', dayjs(tiempoSalida).format('YYYY-MM-DD HH:mm:ss'));      
        
            if (dayjs(simulatedTime).isBefore(dayjs(tiempoSalida))) {
                allFinished = false; // Todavía no es hora de que este camión se mueva
                console.log('Camión todavía no inicia su tramo.');
                return;
            }
    
    
            //Escalar el tiempo de la simulación (1 hora simulada = 10 segundos reales)
            const tiempoTramoSimulado = (distancia / velocidadTramo) * 3600; // Tiempo del tramo en segundos simulados
            const progresoActual = progresoTramoRef.current[rutaIndex];
            console.log(`Progreso actual del tramo (camión ${codigo}):`, progresoActual);
            
            // Ajuste para que el avance sea más rápido y visualmente notable
            const incrementoAvance = (1 / tiempoTramoSimulado) * velocidad * 0.1; // Multiplicador de 0.1 para hacer el avance más significativo
            progresoTramoRef.current[rutaIndex] += incrementoAvance;

            console.log(`Progreso actualizado del tramo (camión ${codigo}):`, progresoTramoRef.current[rutaIndex]);
            
    
            // Calcula la nueva posición del camión
            // Calcula la nueva posición del camión
            const nuevaPosicion = {
                latitud: origen.latitud + (destino.latitud - origen.latitud) * progresoTramoRef.current[rutaIndex],
                longitud: origen.longitud + (destino.longitud - origen.longitud) * progresoTramoRef.current[rutaIndex],
            };
            // Actualiza la posición actual del camión
            setCurrentPositions((prev) => ({
                ...prev,
                [codigo]: nuevaPosicion,
            }));
    
            if (Math.round(progresoTramoRef.current[rutaIndex] * 1000) >= 1000) {
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
            console.log("Simulación terminada");
            onSimulacionTerminada();
        }
    };
    


    useEffect(() => {
        if (rutas.length > 0) {
            // Establecer el progreso y el índice de los tramos
            console.log("Iniciando camiones...");
            tramoIndexRef.current = rutas.map(() => 0); // Índices iniciales de los tramos
            progresoTramoRef.current = rutas.map(() => 0.01); // Progreso inicial por tramo

            // Establece las posiciones iniciales de los camiones
            setCurrentPositions(
                rutas.reduce((acc, ruta) => {
                    const { codigo } = ruta.camion;
                    if (ruta.tramos.length > 0) {
                        acc[codigo] = {
                            latitud: ruta.tramos[0].origen.latitud,
                            longitud: ruta.tramos[0].origen.longitud,
                        };
                    }
                    return acc;
                }, {})
            );
            console.log("Posiciones iniciales:", currentPositions);
        }
    }, [rutas]);

    /*
    useEffect(() => {
        if (simulacionActiva) {
            const interval = setInterval(() => {
                moverCamiones(velocidad, () => {
                    console.log("Simulación detenida desde moverCamiones.");
                    setSimulacionActiva(false);
                });
            }, 50);

            return () => clearInterval(interval);
        }
    }, [simulacionActiva, velocidad, rutas,simulatedTime]);

    */

    useEffect(() => {
        let animationFrameId;
    
        const moverCamionesAnimados = () => {
            moverCamiones(velocidad, onSimulacionTerminada, simulatedTime);
            animationFrameId = requestAnimationFrame(moverCamionesAnimados);
        };
    
        if (simulacionActiva) {
            animationFrameId = requestAnimationFrame(moverCamionesAnimados);
        } else {
            cancelAnimationFrame(animationFrameId);
        }
    
        return () => cancelAnimationFrame(animationFrameId);
    }, [simulacionActiva, velocidad, simulatedTime]);
    


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

    // Debug para asegurarte que `simulatedTime` no sea undefined
    useEffect(() => {
        if (simulatedTime) {
            //console.log("Simulated Time en Simulador actualizado:", simulatedTime.format('YYYY-MM-DD HH:mm:ss'));
        } else {
            console.log("Simulated Time aún no está disponible.");
        }
    }, [simulatedTime]);
    

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
            rutas.forEach((ruta, index) => {
                console.log(`Ruta ${index} tiene ${ruta.tramos.length} tramos:`);
                console.log(ruta.tramos); // el último tramo se pinta?
            });

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
                }, {})
            );
        }
    }, [rutas]); // Se asegura que se actualice cada vez que cambien las rutas

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
                <TablaSimulacion data={rutas}/>
            </div>

            <div className="relative w-7/12 h-100 border border-gray-300 shadow-lg rounded-lg">
                <div className="w-full flex justify-left items-center" style={{height: "5vh", marginTop: "1vh", marginBottom: "1vh"}}>
                    <h1 style={{fontSize: "1rem", fontWeight: '400', marginLeft: "1vh", marginRight: "1.5vh"}}
                        >Eliga el tipo de simulación: 
                    </h1>
                    <Dropdown
                        menu={{
                        items,
                        selectable: true,
                        defaultSelectedKeys: ['2'],
                        }}
                    >
                        <Typography.Link>
                        <Space>
                            {selectedItem}
                            <DownOutlined />
                        </Space>
                        </Typography.Link>
                    </Dropdown>
                    

                    <Space direction="vertical" style={{marginLeft: "5vh"}}>
                        <DatePicker onChange={onChangeDate} disabledDate={disabledDate} defaultValue={dayjs('2024-03-14', 'YYYY-MM-DD')} />
                    </Space>
                    <TimePicker style={{marginLeft: "2vh"}}
                        onChange={onChangeTime} defaultOpenValue={dayjs('00:00:00', 'HH:mm:ss')} 
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
                        currentTime = {currentTime}
                        setCurrentTime = {setCurrentTime}
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
