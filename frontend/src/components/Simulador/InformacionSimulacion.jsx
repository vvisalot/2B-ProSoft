import {Card} from "antd";
import {useState} from "react";

const InformacionSimulacion = () => {
    const [enMantenimiento, setEnMantenimiento] = useState(0);
    const [enMovimiento, setEnMovimiento] = useState(0);

    return (
        <Card title="Informacion de la simulacion" style={{marginRight: "10px"}}>
            <Card type="inner" title="Detalle de la simulacion" style={{marginBottom: "15px"}}>
                <Card.Grid
                    hoverable={false}
                    style={{
                        width: '50%',
                        height: '10%',
                    }}>
                    <p>Fecha: </p>
                </Card.Grid>
                <Card.Grid
                    hoverable={false}
                    style={{
                        width: '50%',
                        height: '10%',
                    }}>
                    <p>Hora: </p>
                </Card.Grid>
                <Card.Grid
                    hoverable={false}
                    style={{
                        width: '100%',
                    }}>
                    <p>Tiempo transcurrido: </p>
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