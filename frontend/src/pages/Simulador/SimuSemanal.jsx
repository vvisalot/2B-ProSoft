import { Button, DatePicker, Row, Col } from "antd";
import { useEffect, useState } from "react";
import CardLeyenda from "../../cards/CardLeyenda";
import MapaPeruSimulacion from "../../components/MapaPeruSimulacion";
import TablaFlota from "../../components/TableFlota";

const SimuSemanal = () => {
    const [currentTime, setCurrentTime] = useState(new Date().toLocaleTimeString());
    const [isSectionVisible, setIsSectionVisible] = useState(false);
    const [isPanelVisible, setIsPanelVisible] = useState(true);

    useEffect(() => {
        const timer = setInterval(() => {
            setCurrentTime(new Date().toLocaleTimeString());
        }, 1000);
        return () => clearInterval(timer);
    }, []);


    const [numCamiones, setNumCamiones] = useState(0);
    const [numRutas, setNumRutas] = useState(0);

    // Función para actualizar los estados de camiones y rutas
    const handleUpdateStats = (camiones, rutas) => {
        setNumCamiones(camiones);
        setNumRutas(rutas);
    };

    return (
        <div className="h-full flex flex-col relative">
            {/*}
            <div className="w-full flex justify-center items-center" style={{ height: "5vh", backgroundColor: '#ffd700' }}>
                <div className="flex justify-between items-center w-full max-w-3xl">
                    <span>Fecha: {new Date().toLocaleDateString()}</span>
                    <span>Hora: {currentTime}</span>
                </div>
            </div>
            */}

            {/* Contenido principal */}
            <div className="flex-1 relative flex">
                {/* Botón para mostrar/ocultar panel */}
                <Button
                    className="absolute top-4 left-0 z-50"
                    onClick={() => setIsPanelVisible(!isPanelVisible)}
                    style={{ 
                        backgroundColor: '#e5e7eb',
                        borderColor: '#e5e7eb',
                    }}
                >
                    {isPanelVisible ? ">>" : "<<"}
                </Button>

                {/* Panel lateral */}
                <div 
                    className="transition-all duration-300 ease-in-out overflow-hidden"
                    style={{
                        width: isPanelVisible ? '33.333%' : '0',
                        minWidth: isPanelVisible ? '300px' : '0',
                    }}
                >
                    {isPanelVisible && (
                        <div className="p-4">
                            <div style={{ display: 'flex', justifyContent: 'center' }}>
                                <h1 style={{ fontSize: "1.5rem", fontWeight: '400' }}>Simulación Semanal</h1>
                            </div>
                            <div className="flex mb-4">
                                <DatePicker placeholder="Fecha Inicio" className="mr-4" />
                                <DatePicker placeholder="Fecha Fin" />
                            </div>
                            <CardLeyenda numCamiones={numCamiones} numRutas={numRutas} />
                            <div className="mt-4">
                                <h1 style={{ fontSize: "1rem", fontWeight: '400' }}>Camiones</h1>
                                <TablaFlota scroll={{ x: 1000 }} />
                            </div>
                        </div>
                    )}
                </div>

                {/* Contenedor del mapa */}
                <div 
                    className="transition-all duration-300 ease-in-out"
                    style={{
                        width: isPanelVisible ? '66.667%' : '100%',
                        padding: '1rem',
                    }}
                >
                    <MapaPeruSimulacion onUpdateStats={handleUpdateStats} />
                </div>
            </div>
        </div>
    );
};

export default SimuSemanal;



{/* desplegable que ya no usare por ahora
                        <div
                            className={`bg-gray-200 p-3 w-full absolute up-0 transition-transform ${isSectionVisible ? "translate-y-0" : "translate-y-full"}`}
                            style={{ zIndex: 10 }}
                        >
                            <Button
                                className="absolute top-[-20px] left-1/2 transform -translate-x-1/2"
                                onClick={() => setIsSectionVisible(!isSectionVisible)}
                                style={{ backgroundColor: '#e5e7eb', borderColor: '#e5e7eb', }}
                            >
                                {isSectionVisible ? "Ocultar" : "Mostrar detalle camiones"}
                            </Button>

                            {isSectionVisible && (
                                <div className="mt-4">
                                    <h1 style={{ fontSize: "1rem", fontWeight: '400' }}>Camiones</h1>
                                    <TablaFlota scroll={{ x: 1000 }} />
                                </div>
                            )}
                        </div>
                        */}