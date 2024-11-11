// src/pages/LandingPage.js
import React from "react";
import logo from "../assets/odipark.svg"; // Asegúrate de que la ruta sea correcta
import imagen from "../assets/icons/optimizacion-de-rutas-de-transporte.png";

const LandingPage = ({ onEnter }) => {
	return (
		<div
			style={{
				display: "flex",
				flexDirection: "column",
				alignItems: "center",
				justifyContent: "center",
				height: "100vh",
				backgroundColor: "#f8f9fa",
				padding: "20px",
			}}
		>
			<header
				style={{
					display: "flex",
					alignItems: "center",
					width: "100%",
					maxWidth: "1200px",
					marginBottom: "40px",
				}}
			>
				<img
					src={logo}
					alt="OdiparPack Logo"
					style={{ height: "40px", marginRight: "16px" }}
				/>
				<h1 style={{ fontSize: "1.5rem", color: "#333" }}>OdiparPack</h1>
			</header>

			<main
				style={{
					display: "flex",
					flexDirection: "row",
					alignItems: "center",
					justifyContent: "center",
					width: "100%",
					maxWidth: "1200px",
					textAlign: "left",
				}}
			>
				<div style={{ flex: 1, padding: "20px" }}>
					<h2 style={{ fontSize: "2.5rem", color: "#333", marginBottom: "20px" }}>
						Te damos la bienvenida a OdiparPack
					</h2>
					<p style={{ fontSize: "1.2rem", color: "#666", marginBottom: "30px" }}>
                    Planifica tus rutas con OdiparPack: la solución eficiente para la entrega 
                    de pedidos en Perú. Empieza hoy y descubre una nueva forma de trabajar.
					</p>
					<button
						onClick={onEnter}
						style={{
							padding: "15px 30px",
							fontSize: "1.1rem",
							color: "#fff",
							backgroundColor: "#6f42c1",
							border: "none",
							borderRadius: "5px",
							cursor: "pointer",
						}}
					>
						Entrar al Servicio
					</button>
				</div>
				<div style={{ flex: 1, display: "flex", justifyContent: "center" }}>
					<img
						src={imagen} // Reemplaza con una imagen representativa si tienes
						alt="Representación visual"
						style={{ borderRadius: "8px" }}
					/>
				</div>
			</main>
		</div>
	);
};

export default LandingPage;
