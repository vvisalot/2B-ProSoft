import { Button, Tabs } from "antd";
import MapaPeru from "../components/MapaPeru";
import TableFlota from "../components/Simulador/TablaFlota.jsx";

const Planificador = () => {
	return (
		<div className="flex h-full">
			<div className="w-full">
                
                <div className="flex justify-between">
                    <div>
                        <Button className="mr-2 bg-black" type="primary">
                            Nuevo Camion
                        </Button>
                        <Button> Importar Flota </Button>
                    </div>
                    <div>
                        <Button style={{ backgroundColor: 'grey', color: 'white' }} className="mr-2" type="primary">
                            Reportar Averia
                        </Button>
                    </div>
                </div>
				<TableFlota />

			</div>
		</div>
	);
};

export default Planificador;
