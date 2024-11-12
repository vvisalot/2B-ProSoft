import {Button, Col, DatePicker, Row, Upload, message } from "antd";
import axios from 'axios';
import { useEffect, useState } from "react";
import CardLeyenda from "/src/cards/CardLeyenda";
import MapaPeruSimulacion from "/src/components/MapaPeruSimulacion";
import TablaFlota from "/src/components/TableFlota";

const Simulador = () => {
    const [currentTime, setCurrentTime] = useState(new Date().toLocaleTimeString());
    const [selectedFile, setSelectedFile] = useState(null);
    const [soluciones, setSoluciones] = useState([]); // Estado para almacenar las soluciones
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

    // Función para manejar la selección de archivo
    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
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
            const response = await axios.post("http://localhost:8080/api/simulated-annealing/soluciones", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });
            
            message.success("Archivo cargado exitosamente");
            console.log("Respuesta del servidor:", response.data);
            // Guardar las soluciones en el estado
            setSoluciones(response.data);
        } catch (error) {
            message.error("Error al cargar el archivo.");
            console.error("Error en la solicitud:", error);
        }
    };   

    return (
        <div className="h-full flex flex-col relative">
            <div className="flex-1 p-4">
                <Row gutter={[16, 16]}>
                    <Col xs={24} md={8}>
                        <h1 style={{ fontSize: "1.5rem", fontWeight: '400' }}>Simulación Semanal</h1>
                        {/* Formulario de carga de archivo */}
                        <input type="file" onChange={handleFileChange} />
                        <Button type="primary" onClick={handleSubmit} style={{ marginTop: "10px" }}>
                            Ejecutar
                        </Button>
                        <CardLeyenda numCamiones={numCamiones} numRutas={numRutas} />
                    </Col>
                    <Col xs={24} md={16}>
                        <MapaPeruSimulacion soluciones={soluciones} onUpdateStats={handleUpdateStats} />
                    </Col>
                </Row>
            </div>
        </div>
    );
};

export default Simulador;
