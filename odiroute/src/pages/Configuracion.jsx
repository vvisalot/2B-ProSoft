import { Menu, Layout } from "antd";
import Sider from "antd/es/layout/Sider";
const { Content } = Layout;

const Configuracion = () => {
	return (
		<Layout>
			<Sider>
				<Menu theme="light" mode="inline" defaultSelectedKeys={["1"]}>
					<Menu.Item key="1">Usuarios</Menu.Item>
					<Menu.Item key="2">Roles</Menu.Item>
					<Menu.Item key="3">Permisos</Menu.Item>
				</Menu>
			</Sider>
			<Content>Hola</Content>
		</Layout>
	);
};

export default Configuracion;
