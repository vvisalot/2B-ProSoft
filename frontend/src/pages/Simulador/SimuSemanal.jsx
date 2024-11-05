import { Button, DatePicker, Row, Col } from "antd";
import { useEffect, useState } from "react";
import CardLeyenda from "../../cards/CardLeyenda";
import MapaPeruSimulacion from "../../components/MapaPeruSimulacion";
import TablaFlota from "../../components/TableFlota";

const SimuSemanal = () => {
    const [currentTime, setCurrentTime] = useState(new Date().toLocaleTimeString());
    const [isSectionVisible, setIsSectionVisible] = useState(false);

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
            <div className="w-full flex justify-center items-center" style={{ height: "5vh", backgroundColor: '#ffd700' }}>
                <div className="flex justify-between items-center w-full max-w-3xl">
                    <span>Fecha: {new Date().toLocaleDateString()}</span>
                    <span>Hora: {currentTime}</span>
                </div>
            </div>

            <div className="flex-1 p-4">
                <Row gutter={[16, 16]}>
                    <Col xs={24} md={8}>
                        <h1 style={{ fontSize: "1.5rem", fontWeight: '400' }}>Simulación Semanal</h1>
                        <div className="flex mb-4">
                            <DatePicker placeholder="Fecha Inicio" className="mr-4" />
                            <DatePicker placeholder="Fecha Fin" />
                        </div>
                        <CardLeyenda numCamiones={numCamiones} numRutas={numRutas} />
                    </Col>
                    <Col xs={24} md={16}>
                        <MapaPeruSimulacion onUpdateStats={handleUpdateStats} />
                    </Col>
                </Row>
            </div>

            <div
                className={`bg-gray-200 p-4 w-full absolute bottom-0 transition-transform ${isSectionVisible ? "translate-y-0" : "translate-y-full"}`}
                style={{ zIndex: 10 }}
            >
                <Button
                    className="absolute top-[-20px] left-1/2 transform -translate-x-1/2"
                    onClick={() => setIsSectionVisible(!isSectionVisible)}
                    style={{ backgroundColor: '#e5e7eb', borderColor: '#e5e7eb', }}
                >
                    {isSectionVisible ? "Ocultar" : "Mostrar"}
                </Button>

                {isSectionVisible && (
                    <div className="mt-4">
                        <h1 style={{ fontSize: "1rem", fontWeight: '400' }}>Camiones</h1>
                        <TablaFlota scroll={{ x: 1000 }} />
                    </div>
                )}
            </div>
        </div>
    );
};

export default SimuSemanal;
