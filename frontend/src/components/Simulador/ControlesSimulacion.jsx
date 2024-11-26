import PropTypes from "prop-types";
import { useEffect } from "react";
import { useControlesSimulacion } from "../../hooks/useControlesSimulacion.jsx";

const ControlesSimulacion = ({
	rutas,
	setRutas,
	moverCamiones,
	currentTime,
	setCurrentTime,
	resetearSimulacion,
	onSimulacionStateChange
}) => {
	const {
		simulacionActiva,
		simulacionIniciada,
		simulacionTerminada,
		resetRequerido,
		velocidad,
		iniciarSimulacion,
		pausarSimulacion,
		reanudarSimulacion,
		pararSimulacion,
		acelerarSimulacion,
		reducirSimulacion
	} = useControlesSimulacion(
		rutas,
		setRutas,
		moverCamiones,
		resetearSimulacion,
		currentTime,
		setCurrentTime
	);

	useEffect(() => {
		onSimulacionStateChange({
			simulacionActiva,
			simulacionIniciada,
			resetRequerido,
			velocidad
		});
	}, [
		onSimulacionStateChange,
		simulacionActiva,
		simulacionIniciada,
		resetRequerido,
		velocidad
	]);

	return (
		<div>
			<div className="flex space-x-2">
				{/* Botón para reducir la velocidad */}
				<button className="px-4 py-2 text-black" onClick={reducirSimulacion}>
					0.5x
				</button>

				{/* Botón de Iniciar/Reanudar/Pausar */}
				<button
					className="px-4 py-2 flex items-center space-x-2"
					onClick={
						simulacionIniciada
							? simulacionActiva
								? pausarSimulacion
								: reanudarSimulacion
							: iniciarSimulacion
					}
				>
					<img
						src={`/src/assets/icons/${simulacionActiva ? "pausar" : "play"}.png`}
						alt={simulacionActiva ? "Pausar" : "Iniciar/Reanudar"}
						className="w-5 h-5"
					/>
				</button>

				{/* Botón de parar la simulación */}
				<button type="button" className="px-4 py-2" onClick={pararSimulacion}>
					<img src="/src/assets/icons/parar.png" alt="Parar" className="w-5 h-5"/>
				</button>

				{/* Botón para aumentar la velocidad */}
				<button type="button"className="px-4 py-2 text-black rounded-lg" onClick={acelerarSimulacion}>
					2x
				</button>
			</div>

			{/* Mostrar la velocidad actual */}
			<div className="mt-2 mb-2 text-blue-700 text-center">
				<p>Velocidad: {velocidad}x</p>
			</div>
		</div>
	);
};

ControlesSimulacion.propTypes = {
	rutas: PropTypes.array.isRequired,
	setRutas: PropTypes.func.isRequired,
	moverCamiones: PropTypes.func.isRequired,
	currentTime: PropTypes.number.isRequired,
	setCurrentTime: PropTypes.func.isRequired,
	resetearSimulacion: PropTypes.func.isRequired,
	onSimulacionStateChange: PropTypes.func.isRequired
};

export default ControlesSimulacion;
