import { Tabs} from "antd";
import TablaFlota from "./TablaFlota.jsx";
import TablaPedidos from "./TablaPedidos.jsx";


const TablaSimulacion = ({data}) => {
    const onChange = (key) => {
        console.log(key);
    }

    const items = [
        // {
        //     key: '1',
        //     label: 'Pedidos',
        //     children: <TablaPedidos data={data}/>
        // },
        {
            key: '2',
            label: 'Camiones',
            children: <TablaFlota data={data}/>
        },
    ];
    return (
        <Tabs
            onChange={onChange}
            type="card"
            items={items}
        >
        </Tabs>
    );
};

export default TablaSimulacion;
