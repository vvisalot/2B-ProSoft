import { DatePicker, Tabs } from "antd";
import Tabla from "../components/Tabla";

const Simulacion = () => {
	return (
		<div className="h-96 bg-stone-600">
			<div className="h-full bg-stone-600" />
			<div className="flex flex-col h-full">
				<Tabs
					defaultActiveKey="1"
					className="w-full"
					items={[
						{
							label: "Por fecha",
							key: "1",
							children: (
								<>
									<div className="flex">
										<div>
											Fecha Inicio
											<DatePicker
												className="ml-4"
												placeholder="Seleccionar fecha"
											/>
										</div>
										<div className="pl-4">
											Fecha Fin
											<DatePicker
												className="ml-4"
												placeholder="Seleccionar fecha"
											/>
										</div>
									</div>
									<Tabla />
								</>
							),
						},
						{
							label: "Colapso",
							key: "2",
							children: (
								<>
									<div className="flex">
										<div>
											Fecha Inicio
											<DatePicker className="ml-4" />
										</div>
										<div className="pl-4">
											Fecha Fin
											<DatePicker className="ml-4" />
										</div>
									</div>
									<Tabla />
								</>
							),
						},
					]}
				/>
			</div>
		</div>
	);
};

export default Simulacion;
