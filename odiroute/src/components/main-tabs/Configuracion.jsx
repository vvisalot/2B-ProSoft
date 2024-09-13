import { Layout, Menu } from "antd";
import React, { useState } from "react";

const { Sider, Content } = Layout;

// Estilos externos
const siderStyle = {
	background: "#ababab",
	boxShadow: "2px 0 5px rgba(0,0,0,0.1)",
	height: "full",
};

const Configuracion = () => {
	return (
		<Layout style={{ height: "100%" }}>
			<Sider className="bg-black" style={siderStyle}>
				valeria
			</Sider>
			<Content>Contenido</Content>
		</Layout>
	);
};

export default Configuracion;
