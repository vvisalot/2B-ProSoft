import { Menu, Layout, Form, Input, Button } from "antd";
const { Content } = Layout;

const Configuracion = () => {
	return (
		<Layout className="h-max flex-row">
			<Menu className="w-[30vh]" defaultSelectedKeys={["1"]}>
				<Menu.Item key="1">Mi cuenta</Menu.Item>
				<Menu.Item key="2">Almacenes</Menu.Item>
				<Menu.Item key="3">Vehiculos</Menu.Item>
			</Menu>

			<Layout>
				<Content
					style={{
						margin: "24px 16px",
						padding: 24,
						minHeight: 280,
					}}
				>
					<div className="ml-6">
						<h1 className="mb-4 text-2xl">Configuración de cuenta</h1>
						<h2 className="mb-4 text-lg">Información general</h2>

						{/* Formulario de Información General */}
						<Form layout="vertical" className="mb-6">
							<Form.Item label="Nombres">
								<Input placeholder="Julio Cesar" />
							</Form.Item>
							<Form.Item label="Apellido Paterno">
								<Input placeholder="Tello" />
							</Form.Item>
							<Form.Item label="Apellido Materno">
								<Input placeholder="Rojas" />
							</Form.Item>
							<Form.Item label="Correo electrónico">
								<Input placeholder="juliotc@odipp.com" />
							</Form.Item>
							<Form.Item label="Teléfono">
								<Input placeholder="963857412" />
							</Form.Item>
						</Form>

						<h2 className="mb-4 text-lg">Seguridad de la cuenta</h2>

						{/* Formulario de Seguridad */}
						<Form layout="vertical">
							<Form.Item label="Contraseña">
								<Input.Password placeholder="************" />
							</Form.Item>
							<Form.Item>
								<Button type="primary" htmlType="submit" className="bg-black">
									Modificar contraseña
								</Button>
							</Form.Item>
						</Form>
					</div>
				</Content>
			</Layout>
		</Layout>
	);
};

export default Configuracion;
