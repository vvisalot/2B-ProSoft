import { Table } from 'antd';


const columnasPedidos = [
    { title: "Fecha", dataIndex: "fecha", key: "fecha" },
    { title: "Hora", dataIndex: "hora", key: "hora" },
    { title: "ID Cliente", dataIndex: "idCliente", key: "idCliente" },
    { title: "Cantidad Total", dataIndex: "cantidadTotal", key: "cantidadTotal" },
];


const TablaPedidos = ({data, columns}) => {
    const formattedData = data.flatMap((item) =>
        item.camion.paquetes.map((pedido) => {
            const fechaHora = new Date(pedido.fechaHoraPedido);
            const fecha = fechaHora.toLocaleDateString(); // Obtiene la fecha en formato local (dd/mm/aaaa o mm/dd/aaaa)
            const hora = fechaHora.toLocaleTimeString();   // Obtiene la hora en formato local (hh:mm:ss)

            return {
                key: pedido.codigo,
                fecha: fecha,
                hora: hora,
                idCliente: pedido.idCliente,
                cantidadTotal: pedido.cantidadTotal,
            };
        })
    );


    return (
        <Table
            className="pt-4"
            dataSource={formattedData}
            columns={columnasPedidos}
            scroll={{ x: 100 }}
        />
    );
}

export default TablaPedidos;