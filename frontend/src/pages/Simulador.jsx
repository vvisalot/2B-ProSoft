import {React, useEffect, useRef, useState} from "react";
import {Button, Input, Modal, DatePicker, TimePicker, Space, Dropdown, Typography} from "antd";
import { DownOutlined } from '@ant-design/icons';
import rutaData from "/src/assets/data/Data.json";
import MapaSimulacion from "/src/components/Simulador/MapaSimulacion";
import ControlesSimulacion from "../components/Simulador/ControlesSimulacion.jsx";
import TablaSimulacion from "../components/Simulador/TablaSimulacion.jsx";
import log from "eslint-plugin-react/lib/util/log.js";
import InformacionSimulacion from "../components/Simulador/InformacionSimulacion.jsx"; // Asegúrate de importar Papa Parse
import dayjs from 'dayjs';
import customParseFormat from 'dayjs/plugin/customParseFormat';
dayjs.extend(customParseFormat);

const Simulador = () => {
    const [currentTime, setCurrentTime] = useState(new Date().toLocaleTimeString());
    const [numCamiones, setNumCamiones] = useState(0);
    const [numRutas, setNumRutas] = useState(0);

    const [rutas, setRutas] = useState(rutaData);

    const tramoIndexRef = useRef([]);
    const progresoTramoRef = useRef([]);
    const [currentPositions, setCurrentPositions] = useState({});

    const [simulacionActiva, setSimulacionActiva] = useState(false);
    const [simulacionIniciada, setSimulacionIniciada] = useState(false);
    const [resetRequerido, setResetRequerido] = useState(false); // Nuevo estado
    const [velocidad, setVelocidad] = useState(1); // Multiplicador de velocidad
    
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

    const onChangeDate = (date, dateString) => {
        console.log("Selected Date:", date);
        console.log("Formatted Date String:", dateString);
    };
    const onChangeTime = (time, timeString) => {
      console.log(time, timeString);
    };

    const moverCamiones = (velocidad, onSimulacionTerminada) => {
        let allFinished = true;

        rutas.forEach((ruta, rutaIndex) => {
            const {codigo} = ruta.camion;
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

            setCurrentPositions((prev) => {
                return {
                    ...prev,
                    [codigo]: nuevaPosicion
                };
            });

            progresoTramoRef.current[rutaIndex] += (1 / tiempoTramo) * velocidad;

            if (progresoTramoRef.current[rutaIndex] >= 1) {
                tramoIndexRef.current[rutaIndex]++;
                progresoTramoRef.current[rutaIndex] = 0.01;

                if (tramoIndexRef.current[rutaIndex] >= ruta.tramos.length) {
                    setCurrentPositions((prev) => {
                        const updated = {...prev};
                        delete updated[codigo];
                        return updated;
                    });
                    tramoIndexRef.current[rutaIndex] = -1; // Marcar el camión como terminado
                } else allFinished = false;
            } else allFinished = false;
        });
        if (allFinished) {
            console.log("Simulación terminada");
            onSimulacionTerminada();
        }
    };


    const resetearSimulacion = () => {
        tramoIndexRef.current = rutas.map(() => 0);
        progresoTramoRef.current = rutas.map(() => 0);
        setCurrentPositions(
            rutas.reduce((acc, ruta) => {
                const {codigo} = ruta.camion;
                acc[codigo] = {
                    latitud: ruta.tramos[0].origen.latitud,
                    longitud: ruta.tramos[0].origen.longitud
                };
                return acc;
            }, {})
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
            rutaData.reduce((acc, ruta) => acc + ruta.tramos.length, 0)
        );
    }, []);

    //Se cargan los datos de la solucion del algoritmo.
    useEffect(() => {
        if (rutas.length > 0) {
            for (const ruta of rutaData) {
                const fix = {...ruta.tramos[0]};
                fix.destino = {...fix.origen};
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

    // Función para actualizar los estados de camiones y rutas
    const handleUpdateStats = (camiones, rutas) => {
        setNumCamiones(camiones);
        setNumRutas(rutas);
    };

    return (
        <div className="h-fit flex p-2">
            <div className="w-5/12">
                <TablaSimulacion data={rutas}/>
                <InformacionSimulacion/>
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
                        <DatePicker onChangeDate={onChangeDate} />
                    </Space>
                    <TimePicker style={{marginLeft: "2vh"}}
                        onChangeTime={onChangeTime} defaultOpenValue={dayjs('00:00:00', 'HH:mm:ss')} 
                    />

                </div>
            
                <MapaSimulacion
                    rutas={rutas}
                    currentPositions={currentPositions}
                    tramoIndexRef={tramoIndexRef}
                    progresoTramoRef={progresoTramoRef}
                    onUpdateStats={handleUpdateStats}
                    simulacionActiva={simulacionActiva}
                    simulacionIniciada={simulacionIniciada}
                    resetRequerido={resetRequerido}
                    velocidad={velocidad}
                />
                <div className="absolute bottom-4 right-4 z-10 bg-white p-4 rounded-lg shadow-lg">
                    <ControlesSimulacion
                        rutas={rutas}
                        moverCamiones={moverCamiones}
                        resetearSimulacion={resetearSimulacion}
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
