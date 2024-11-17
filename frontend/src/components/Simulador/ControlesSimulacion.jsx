const ControlesSimulacion = ({
                                 simulacionActiva,
                                 simulacionIniciada, // Nuevo estado para verificar si ya inició
                                 pausarSimulacion,
                                 reanudarSimulacion,
                                 iniciarSimulacion, // Nueva función para iniciar la simulación
                                 pararSimulacion, // Nueva función para parar la simulación
                                 acelerarSimulacion,
                                 reducirSimulacion,
                                 velocidad,
                             }) => {
    return (
        <div>
            <div className="flex space-x-2">
                {/* Botón para reducir la velocidad */}
                <button
                    className="px-4 py-2 text-black"
                    onClick={reducirSimulacion}
                >
                    0.5x
                </button>

                {/* Botón de Iniciar/Reanudar/Pausar */}
                <button
                    className="px-4 py-2 flex items-center space-x-2"
                    onClick={simulacionIniciada ? (simulacionActiva ? pausarSimulacion : reanudarSimulacion) : iniciarSimulacion}
                >
                    <img
                        src={`/src/assets/icons/${simulacionActiva ? "pausar" : "play"}.png`}
                        alt={simulacionActiva ? "Pausar" : "Iniciar/Reanudar"}
                        className="w-5 h-5"
                    />
                </button>

                {/* Botón de parar la simulación */}
                <button
                    className="px-4 py-2"
                    onClick={pararSimulacion}
                >
                    <img src="/src/assets/icons/parar.png" alt="Parar" className="w-5 h-5"/>
                </button>

                {/* Botón para aumentar la velocidad */}
                <button
                    className="px-4 py-2 text-black rounded-lg"
                    onClick={acelerarSimulacion}
                >
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

export default ControlesSimulacion;