import { Menu } from "antd";
import { Layout } from "antd";
const { Content } = Layout;

const Analiticas = () => {
	// Your component logic goes here

	return (
		<Layout className="h-max flex-row">
			<Menu className="w-[30vh]" defaultSelectedKeys={["1"]}>
				<Menu.Item key="1">Pedidos</Menu.Item>
				<Menu.Item key="2">Simulacion</Menu.Item>
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
						<h1 className="mb-4 text-2xl">Dashboard</h1>
					</div>
				</Content>
			</Layout>
		</Layout>
	);
};

export default Analiticas;
