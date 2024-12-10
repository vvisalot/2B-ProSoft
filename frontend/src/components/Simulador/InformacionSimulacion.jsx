import {Card} from "antd";
import dayjs from "dayjs";
import {useEffect, useRef, useState } from "react";

const InformacionSimulacion = ({ currentTime, simulacionActiva, velocidad, setSimulatedTime }) => {
    const [enMantenimiento, setEnMantenimiento] = useState(0);
    const [enMovimiento, setEnMovimiento] = useState(0);
    const [simulatedTime, setLocalSimulatedTime] = useState(dayjs(currentTime));
    const [timeElapsed, setTimeElapsed] = useState(0); // En horas simuladas
    const startTimeRef = useRef(null); // Hora de inicio en tiempo real
    const animationFrameRef = useRef(null);

    const updateSimulatedTime = () => {
        if (!startTimeRef.current) return;
        const now = Date.now();
        const elapsedRealTime = (now - startTimeRef.current) / 1000; // Segundos reales transcurridos
        
        // Aquí ajustamos la relación: 1 hora simulada = 10 segundos reales
        const elapsedSimulatedTime = elapsedRealTime * (6 / 60) * velocidad; // Horas simuladas (1 hora simulada por cada 10 segundos reales)
    
        const newSimulatedTime = dayjs(currentTime).add(elapsedSimulatedTime, 'hour'); // Sumar las horas simuladas
    
        setLocalSimulatedTime(newSimulatedTime); 
        setSimulatedTime(newSimulatedTime);
        setTimeElapsed(elapsedSimulatedTime);
        animationFrameRef.current = requestAnimationFrame(updateSimulatedTime);
    };
    
    useEffect(() => {
        if (simulacionActiva) {
          startTimeRef.current = Date.now(); // Tiempo real al iniciar simulación
          animationFrameRef.current = requestAnimationFrame(updateSimulatedTime);
        } else {
          cancelAnimationFrame(animationFrameRef.current); // Detener la animación si la simulación no está activa
        }
    
        return () => cancelAnimationFrame(animationFrameRef.current); // Limpiar cuando se desmonte
    }, [simulacionActiva, velocidad, currentTime]);


    return (
        <Card title="Informacion de la simulacion" style={{marginRight: "10px"}}>
            <Card type="inner" title="Detalle de la simulacion" style={{marginBottom: "15px"}}>
                <Card.Grid
                    hoverable={false}
                    style={{
                        width: '50%',
                        height: '10%',
                    }}>
                    <p>Fecha: {simulatedTime.format('YYYY-MM-DD')}</p>
                </Card.Grid>
                <Card.Grid
                    hoverable={false}
                    style={{
                        width: '50%',
                        height: '10%',
                    }}>
                    <p>Hora: {simulatedTime.format('HH:mm:ss')}</p>
                </Card.Grid>
                <Card.Grid
                    hoverable={false}
                    style={{
                        width: '100%',
                    }}>
                    <p>Tiempo transcurrido: {timeElapsed.toFixed(2)} horas simuladas</p>
                </Card.Grid>
            </Card>

            <Card type="inner" title="Detalle de los camiones" style={{marginBottom: "15px"}}>
                <Card.Grid
                    hoverable={false}
                    style={{
                        width: '50%',
                    }}>
                    <p>En movimiento: 0</p>
                </Card.Grid>
                <Card.Grid
                    hoverable={false}
                    style={{
                        width: '50%',
                    }}>
                    <p>En mantenimiento: 0</p>
                </Card.Grid>
            </Card>

            <Card type="inner" title="Detalle de los pedidos" >
                <Card.Grid
                    hoverable={false}
                    style={{
                        width: '50%',
                    }}>
                    <p>Entregados: 0</p>
                </Card.Grid>
                <Card.Grid
                    hoverable={false}
                    style={{
                        width: '50%',
                    }}>
                    <p>Pendientes: 0</p>
                </Card.Grid>
            </Card>
        </Card>
    );
}

export default InformacionSimulacion;