import { Button, DatePicker, Row, Col } from "antd";
import { useEffect, useState } from "react";
import CardLeyenda from "/src/cards/CardLeyenda";
import MapaPeruSimulacion from "/src/components/MapaPeruSimulacion";
import TablaFlota from "/src/components/TableFlota";

const Simulador = () => {
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
            <div className="flex-1 p-4">
                <Row gutter={[16, 16]}>
                    <Col xs={24} md={8}>
                        <h1 style={{ fontSize: "1.5rem", fontWeight: '400' }}>Simulación Semanal</h1>

                        <CardLeyenda numCamiones={numCamiones} numRutas={numRutas} />


                    </Col>
                    <Col xs={24} md={16}>
                        <MapaPeruSimulacion onUpdateStats={handleUpdateStats} />
                    </Col>
                </Row>
            </div>
        </div>
    );
};

export default Simulador;
